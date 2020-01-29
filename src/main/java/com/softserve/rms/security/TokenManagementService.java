package com.softserve.rms.security;

import com.softserve.rms.dto.JwtClaimsDto;
import com.softserve.rms.dto.JwtDto;
import com.softserve.rms.exceptions.JwtExpiredTokenException;
import com.softserve.rms.exceptions.Message;
import com.softserve.rms.exceptions.RefreshTokenException;
import com.softserve.rms.service.impl.PersonServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TokenManagementService implements Message {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenManagementService.class);
    private static Map<String, Pair<Long, String>> tokenMap = new HashMap<>();

    @Value("${secretKey}")
    private String secretKey;

    @Value("${expireTimeAccessToken}")
    private String expireTimeAccessToken;

    @Value("${expireTimeRefreshToken}")
    private String expireTimeRefreshToken;

    private PersonServiceImpl personService;

    public TokenManagementService(@Autowired PersonServiceImpl personService){
        this.personService=personService;
    }

    /**
     * Generates a JWT access and refresh tokens containing userId as claim. These properties are taken from the specified
     *  User object. Access token validity is 1 min, refresh token validity 60 days.
     * @param jwtClaimsDto
     * @return
     */
    public JwtDto generateTokenPair(JwtClaimsDto jwtClaimsDto) {

        long nowMillis=System.currentTimeMillis();
        long expirationTime =Long.parseLong(expireTimeAccessToken);
        Date expiryDate = new Date(nowMillis+expirationTime);
        SignatureAlgorithm signatureAlgorithm=SignatureAlgorithm.HS256;
        byte[] decodeSecretKey = TextCodec.BASE64.decode(secretKey);

        String token = Jwts.builder()
                .claim("userClaims", jwtClaimsDto)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(signatureAlgorithm, decodeSecretKey).compact();



        long expirationTimeRefresh = Long.parseLong(expireTimeRefreshToken);
        String refreshToken=Jwts.builder()
                .claim("userClaims", jwtClaimsDto)
                .setIssuedAt(new Date())
                .setExpiration(new Date(nowMillis+expirationTimeRefresh))
                .signWith(signatureAlgorithm, decodeSecretKey).compact();

        tokenMap.put(refreshToken, new Pair<>(jwtClaimsDto.getUserId(), token));

        return new JwtDto(token, refreshToken);
    }

    /**
     * Refresh access and refresh tokens, using refresh token
     * @param jwtDto
     * @return JwtDto
     */

    public JwtDto refreshTokens(JwtDto jwtDto) {
        Pair<Long, String> idToTokenPair = tokenMap.get(jwtDto.getRefreshToken());

        if (Objects.nonNull(idToTokenPair) && idToTokenPair.getValue().equals(jwtDto.getAccessToken())) {
            JwtDto newPairOfToken = generateTokenPair(new JwtClaimsDto((idToTokenPair.getKey())));
            tokenMap.remove(jwtDto.getRefreshToken());
            tokenMap.put(newPairOfToken.getRefreshToken(), new Pair<>(idToTokenPair.getKey(),newPairOfToken.getAccessToken()));
            return newPairOfToken;
        } else {
            throw new RefreshTokenException(REFRESH_TOKEN_EXCEPTION);
        }
    }

    public Authentication getAuthentication(String token) {
        JwtClaimsDto jwtClaimsDto=parseJwtToken(token);
        String email=personService.getById(jwtClaimsDto.getUserId()).getEmail();
        UserDetails userDetails = personService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails,
                "", userDetails.getAuthorities());
    }


    /**
     * Tries to parse specified String as a JWT access token. If successful, returns User_id.
     *  If unsuccessful (token is invalid or not containing all required user properties), simply returns exception.
     * @param  accessToken accesstoken
     * @return userId
     */

    public JwtClaimsDto parseJwtToken(String accessToken){
        try {
            //decode jwt
            Jws<Claims> claims = Jwts.parser().setSigningKey(TextCodec.BASE64.decode(secretKey)).parseClaimsJws(accessToken);

            LinkedHashMap userClaims = ((LinkedHashMap) claims.getBody().get("userClaims"));

            Long userId = Long.parseLong(userClaims.get("userId").toString());

            return new JwtClaimsDto(userId);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex){
            LOGGER.error("Invalid JWT Token", ex);
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        }  catch (ExpiredJwtException ex) {
            LOGGER.info("JWT Token is expired", ex);
            throw new JwtExpiredTokenException("JWT Token expired");
        }
    }

    /**
     * Method verify whether the token has expired or not
     * @param token
     * @return boolean
     */

    public boolean validateToken(String token) {
        try{
            Claims body = Jwts.parser().setSigningKey(TextCodec.BASE64.decode(secretKey)).parseClaimsJws(token).getBody();
            if (body.getExpiration().before(new Date())){
                return false;
            } else {
                return true;
            }
        }catch(JwtException | IllegalArgumentException e){
            LOGGER.info("dont good data");
            throw new JwtExpiredTokenException("dont good data");
        }
    }
}
