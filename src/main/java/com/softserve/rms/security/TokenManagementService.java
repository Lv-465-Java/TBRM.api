package com.softserve.rms.security;

import com.softserve.rms.dto.JwtDto;
import com.softserve.rms.exceptions.JwtAuthenticationException;
import com.softserve.rms.exceptions.Message;
import com.softserve.rms.exceptions.RefreshTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Class that provides methods for working with JWT.
 * @author Kravets Maryana
 */
@Component
public class TokenManagementService implements Message {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenManagementService.class);

    @Value("${secretKey}")
    private String secretKey;

    @Value("${expireTimeAccessToken}")
    private String expireTimeAccessToken;

    @Value("${expireTimeRefreshToken}")
    private String expireTimeRefreshToken;

    private UserPrincipalDetailsService userPrincipalDetailsService;

    /**
     * constructor
     * @param userPrincipalDetailsService {@link UserPrincipalDetailsService}
     */
    @Autowired
    public TokenManagementService(UserPrincipalDetailsService userPrincipalDetailsService){
        this.userPrincipalDetailsService=userPrincipalDetailsService;
    }

    /**
     * Generates a JWT access and refresh tokens containing userId as claim. These properties are taken from the specified
     *  User object. Access token validity is 2 min, refresh token validity 60 days.
     * @param email {@link String}
     * @return JwtDto - it is access and refresh tokens
     */
    public JwtDto generateTokenPair(String email) {

        long nowMillis=System.currentTimeMillis();
        long expirationTime =Long.parseLong(expireTimeAccessToken);
        Date expiryDate = new Date(nowMillis+expirationTime);
        SignatureAlgorithm signatureAlgorithm=SignatureAlgorithm.HS256;
        byte[] decodeSecretKey = TextCodec.BASE64.decode(secretKey);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(signatureAlgorithm, decodeSecretKey).compact();


        long expirationTimeRefresh = Long.parseLong(expireTimeRefreshToken);
        String refreshToken=Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(nowMillis+expirationTimeRefresh))
                .signWith(signatureAlgorithm, decodeSecretKey).compact();

        return new JwtDto(token, refreshToken);
    }

    /**
     * Refresh access and refresh tokens, using refresh token
     * @param refreshToken {@link String}
     * @return JwtDto
     */

    public JwtDto refreshTokens(String  refreshToken) {
        if (refreshToken!=null && validateToken(refreshToken)) {
            try {
                String email=getUserEmail(refreshToken);
                JwtDto jwtDto= generateTokenPair(email);
                LOGGER.info("tokens refreshed successfully!");

                return jwtDto;
            }
            catch (JwtException e) {
                throw new RefreshTokenException(REFRESH_TOKEN_EXCEPTION);
            }
        } else {
            throw new JwtAuthenticationException(JWT_AUTHENTICATION_EXCEPTION);
        }
    }

    /**
     * Method that provide authentication.
     *
     * @param token {@link String} - jwt access token.
     * @return {@link Authentication} if user successfully authenticated.
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userPrincipalDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails,
                "", userDetails.getAuthorities());
    }


    /**
     * Gets email from token and throws an error if token is expired.
     * @param token {@link String}
     * @return email {@link String}
     */
    public String getUserEmail(String token) {
            return Jwts.parser().setSigningKey(TextCodec.BASE64.decode(secretKey))
                    .parseClaimsJws(token).getBody().getSubject();
    }


    /**
     * Method verify whether the token has expired or not.
     * @param token {@link String}
     * @return boolean
     */
    public boolean validateToken(String token) {
        boolean isValid = false;
        try{
            Jws<Claims> claimsJws=Jwts.parser().setSigningKey(TextCodec.BASE64.decode(secretKey)).parseClaimsJws(token);
            if (!claimsJws.getBody().getExpiration().before(new Date())){
                isValid=true;
            }
        }catch(IllegalArgumentException | UnsupportedJwtException | MalformedJwtException | SignatureException ex){
            LOGGER.info("Token is not valid!");
        }
        return isValid;
    }


    /**
     * Method that get access token from {@link HttpServletRequest}.
     *
     * @param request this is your request.
     * @return {@link String} of token or null.
     */
    public String resolveAccessToken(HttpServletRequest request) {
        String AUTH_HEADER_PREFIX="Bearer ";
        String AUTHORIZATION_HEADER="Authorization";
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (Objects.nonNull(bearerToken) && bearerToken.startsWith(AUTH_HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Method that get refresh token from {@link HttpServletRequest}.
     *
     * @param request this is your request.
     * @return {@link String} of token or null.
     */
    public String resolveRefreshToken(HttpServletRequest request) {
        String REFRESH_HEADER="RefreshToken";
        String refreshToken = request.getHeader(REFRESH_HEADER);
        if (Objects.nonNull(refreshToken)) {
            return refreshToken;
        }
        return null;
    }
}
