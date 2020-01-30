package com.softserve.rms.security;

import com.softserve.rms.dto.JwtDto;
import com.softserve.rms.exceptions.JwtAuthenticationException;
import com.softserve.rms.exceptions.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 * @author Kravets Maryana
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter implements Message {

    private static final Logger LOGGER= LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private  TokenManagementService tokenManagementService;
    private String AUTH_HEADER_PREFIX="Bearer ";
    private String AUTHORIZATION_HEADER="Authorization";
    private String REFRESH_HEADER="RefreshToken";

    /**
     * constructor
     * @param tokenManagementService
     */
    public JwtAuthorizationFilter(@Autowired TokenManagementService tokenManagementService) {
        this.tokenManagementService = tokenManagementService;
    }

    /**
     * Checks if request has tokens in header, if those tokens still valid, and set
     * authentication for spring.
     *
     * @param request this is servlet that take request
     * @param response this is response servlet
     * @param filterChain this is filter of chain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken=resolveAccessToken(request);
        String refreshToken=resolveRefreshToken(request);

        if (accessToken!=null && refreshToken!=null){
            try {
                if(tokenManagementService.validateToken(accessToken)){
                    Authentication authentication =
                            tokenManagementService.getAuthentication(accessToken);
                    LOGGER.info("User successfully authenticate - {}", authentication.getPrincipal());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    if(tokenManagementService.validateToken(refreshToken)){
                        JwtDto jwtDto=tokenManagementService.refreshTokens(new JwtDto(accessToken,refreshToken));
                        LOGGER.info("token refreshed successfully");

                        response.setHeader(AUTHORIZATION_HEADER,AUTH_HEADER_PREFIX +jwtDto.getAccessToken());
                        response.setHeader(REFRESH_HEADER, jwtDto.getRefreshToken());
                    } else {
                        LOGGER.info("JWT Tokens are expired. Please login");
                        //throw new JwtExpiredTokenException(JWT_EXPIRE_TOKEN_EXCEPTION);
                    }
                }

            } catch (JwtAuthenticationException e){
                LOGGER.info("JWT Authentication ex");
            }

        }

        filterChain.doFilter(request,response);
    }

    /**
     * Method that get access token from {@link HttpServletRequest}.
     *
     * @param request this is your request.
     * @return {@link String} of token or null.
     */
    private String resolveAccessToken(HttpServletRequest request) {
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
    private String resolveRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_HEADER);
        if (Objects.nonNull(refreshToken)) {
            return refreshToken;
        }
        return null;
    }
}
