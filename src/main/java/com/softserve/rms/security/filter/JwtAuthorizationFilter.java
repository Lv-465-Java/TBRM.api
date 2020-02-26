package com.softserve.rms.security.filter;

import com.softserve.rms.exceptions.JwtAuthenticationException;
import com.softserve.rms.exceptions.JwtExpiredTokenException;
import com.softserve.rms.exceptions.Message;
import com.softserve.rms.security.TokenManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 *
 * @author Kravets Maryana
 */
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter implements Message {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private TokenManagementService tokenManagementService;

    /**
     * constructor
     *
     * @param tokenManagementService {@link TokenManagementService}
     */
    @Autowired
    public JwtAuthorizationFilter(TokenManagementService tokenManagementService){
        this.tokenManagementService = tokenManagementService;
    }

    /**
     * Checks if request has tokens in header, if those tokens still valid, and set
     * authentication for spring.
     *
     * @param request     this is servlet that take request
     * @param response    this is response servlet
     * @param filterChain this is filter of chain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = tokenManagementService.resolveAccessToken(request);

        if (accessToken != null) {
            try {
                if (tokenManagementService.validateToken(accessToken)) {
                    Authentication authentication =
                            tokenManagementService.getAuthentication(accessToken);
                    LOGGER.info("User successfully authenticate - {}", authentication.getPrincipal());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtExpiredTokenException e) {
                LOGGER.info("Token has expired: " + accessToken);

            } catch (JwtAuthenticationException e) {
                LOGGER.info("JWT Authentication failed");
            }

        }
        filterChain.doFilter(request, response);
    }
}
