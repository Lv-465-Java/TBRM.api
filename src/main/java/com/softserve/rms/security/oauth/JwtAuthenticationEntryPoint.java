package com.softserve.rms.security.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * This class is invoked when a user tries to access a protected resource without authentication.
 * In this case, we simply return a 401 Unauthorized response
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    /**
     * This method is invoked when a user tries to access a protected resource without authentication,return a 401 Unauthorized response
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param authException {@link AuthenticationException}
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        LOGGER.error("Responding with unauthorized error. Message - {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                authException.getLocalizedMessage());
    }
}
