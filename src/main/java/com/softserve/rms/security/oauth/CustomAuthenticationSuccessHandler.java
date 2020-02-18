package com.softserve.rms.security.oauth;

import com.softserve.rms.dto.JwtDto;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.security.TokenManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * On successful authentication, Spring security invokes the onAuthenticationSuccess() method of
 * the OAuth2AuthenticationSuccessHandler configured in SecurityConfig.
 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private UserRepository userRepository;
    private TokenManagementService tokenManagementService;

    @Autowired
    public CustomAuthenticationSuccessHandler(UserRepository userRepository,
                                              TokenManagementService tokenManagementService) {
        this.userRepository=userRepository;
        this.tokenManagementService=tokenManagementService;
    }

    public CustomAuthenticationSuccessHandler(String defaultTargetUrl) {
        super(defaultTargetUrl);
    }

    private String homeUrl = "http://localhost:8080/";
    private String AUTHORIZATION_HEADER="Authorization";
    private String REFRESH_HEADER="RefreshToken";
    private String AUTH_HEADER_PREFIX="Bearer ";

    /**
     * In this method, create a JWT authentication tokens, and redirect the user to
     * the redirect_uri specified by the client with the JWT token added in the header
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        if (response.isCommitted()) {
            return;
        }
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        Map attributes = oidcUser.getAttributes();
        String email = (String) attributes.get("email");
        User user = userRepository.findUserByEmail(email).orElseThrow(()->new NotFoundException("not found"));
        JwtDto jwtDto =tokenManagementService.generateTokenPair(user.getEmail());
        String redirectionUrl = UriComponentsBuilder.fromUriString(homeUrl)
                .build().toUriString();
        response.setHeader(AUTHORIZATION_HEADER, AUTH_HEADER_PREFIX + jwtDto.getAccessToken());
        response.setHeader(REFRESH_HEADER, jwtDto.getRefreshToken());
        getRedirectStrategy().sendRedirect(request, response, redirectionUrl);
    }

}
