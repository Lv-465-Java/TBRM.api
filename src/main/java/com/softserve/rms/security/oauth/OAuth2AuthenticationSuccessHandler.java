package com.softserve.rms.security.oauth;

import com.softserve.rms.dto.JwtDto;
import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.BadRequestException;
import com.softserve.rms.exceptions.Message;
import com.softserve.rms.exceptions.OAuth2AuthenticationProcessingException;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.security.TokenManagementService;
import com.softserve.rms.security.UserPrincipal;
import com.softserve.rms.security.oauth.user.OAuth2UserInfo;
import com.softserve.rms.util.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static com.softserve.rms.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * On successful authentication, Spring security invokes the onAuthenticationSuccess() method of
 * the OAuth2AuthenticationSuccessHandler configured in SecurityConfig.
 */
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements Message {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);
    @Value("${authorizedRedirectUri}")
    private String authorizedRedirectUri;

    private TokenManagementService tokenManagementService;
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    /**
     * constructor
     * @param tokenManagementService {@link TokenManagementService}
     * @param httpCookieOAuth2AuthorizationRequestRepository {@link HttpCookieOAuth2AuthorizationRequestRepository}
     */
    @Autowired
    OAuth2AuthenticationSuccessHandler(TokenManagementService tokenManagementService,
                                       HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.tokenManagementService=tokenManagementService;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    /**
     * In this method, we perform some validations, create a JWT authentication and refresh tokens,
     * and redirect the user to the redirect_uri specified by the client with the JWT
     * tokens added in the query string
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param authentication {@link Authentication}
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        String AUTHORIZATION_HEADER="authorization";
        String REFRESH_HEADER="refreshToken";
        String AUTH_HEADER_PREFIX="Bearer ";

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        UserPrincipal userPrincipal=(UserPrincipal) authentication.getPrincipal();

        Map attributes = userPrincipal.getAttributes();
        String email = (String) attributes.get("email");

        JwtDto jwtDto=tokenManagementService.generateTokenPair(email);

        String redirectionUrl =UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam(AUTHORIZATION_HEADER,AUTH_HEADER_PREFIX+ jwtDto.getAccessToken())
                .queryParam(REFRESH_HEADER, jwtDto.getRefreshToken())
                .build().toUriString();

        return redirectionUrl;
    }

    /**
     * method delete authorization request cookie
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    /**
     * method verify if authorization redirect uri exist
     * @param uri {@link String}
     * @return boolean {@link Boolean}
     */
    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        URI authorizedURI = URI.create(authorizedRedirectUri);
        if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                && authorizedURI.getPort() == clientRedirectUri.getPort()) {
            return true;
        }
        return false;
    }
}
