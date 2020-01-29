package com.softserve.rms.security;

import com.softserve.rms.dto.JwtDto;
import com.softserve.rms.exceptions.JwtAuthenticationException;
import com.softserve.rms.exceptions.JwtExpiredTokenException;
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
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter implements Message {

    private static final Logger LOGGER= LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private  TokenManagementService tokenManagementService;
    private String AUTH_HEADER_PREFIX="Bearer ";
    private String AUTHORIZATION_HEADER="Authorization";
    private String REFRESH_HEADER="RefreshToken";

    public JwtAuthorizationFilter(@Autowired TokenManagementService tokenManagementService) {
        this.tokenManagementService = tokenManagementService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken=resolveAccessToken(request);
        String refreshToken=resolveRefreshToken(request);

        if (accessToken!=null && refreshToken!=null){
            try {
                if(tokenManagementService.validateToken(accessToken)){
                    Authentication authentication =
                            tokenManagementService.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    if(tokenManagementService.validateToken(refreshToken)){
                        JwtDto jwtDto=tokenManagementService.refreshTokens(new JwtDto(accessToken,refreshToken));

                        response.setHeader("Authorization",AUTH_HEADER_PREFIX +jwtDto.getAccessToken());
                        response.setHeader("RefreshToken", jwtDto.getRefreshToken());
                    } else {
                        LOGGER.info("JWT Token is eeeexpired");
                        throw new JwtExpiredTokenException(JWT_EXPIRE_TOKEN_EXCEPTION);
                    }
                }

            } catch (JwtAuthenticationException e){
                LOGGER.info("JWT Authentication ex");
            }

        }

        filterChain.doFilter(request,response);
    }

    private String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (Objects.nonNull(bearerToken) && bearerToken.startsWith(AUTH_HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }


    private String resolveRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_HEADER);
        if (Objects.nonNull(refreshToken)) {
            return refreshToken;
        }
        return null;
    }
}
