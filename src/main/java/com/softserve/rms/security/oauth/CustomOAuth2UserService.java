package com.softserve.rms.security.oauth;

import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.OAuth2AuthenticationProcessingException;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.security.UserPrincipal;
import com.softserve.rms.security.oauth.user.OAuth2UserInfo;
import com.softserve.rms.security.oauth.user.OAuth2UserInfoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class retrieves the details of the authenticated user
 *
 * @author Kravets Maryana
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private UserRepository userRepository;

    public Boolean isNewUser=false;

    /**
     * constructor
     *
     * @param userRepository {@link UserRepository}
     */
    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * This method is called after an access token is obtained from the OAuth2 provider.
     *
     * @param oAuth2UserRequest {@link OAuth2UserRequest}
     * @return oAuth2User {@link OAuth2User}
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * This method process oAuth2User. If a user with the same email already exists in database then we update his details, otherwise, we register a new user.
     *
     * @param oAuth2UserRequest {@link OAuth2UserRequest}
     * @param oAuth2User        {@link OAuth2User}
     * @return oAuthUser {@link OAuth2User}
     */
    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findUserByEmail(oAuth2UserInfo.getEmail());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getProvider().equals(oAuth2UserRequest.getClientRegistration().getRegistrationId())) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
            isNewUser=false;
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
            isNewUser=true;
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    /**
     * Method for register new user
     *
     * @param oAuth2UserRequest {@link OAuth2UserRequest}
     * @param oAuth2UserInfo    {@link OAuth2UserInfo}
     * @return user {@link User}
     */
    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setProvider(oAuth2UserRequest.getClientRegistration().getRegistrationId());
        user.setProviderId(oAuth2UserInfo.getId());
        user.setFirstName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setRole(new Role(5L, "ROLE_GUEST"));
        user.setEnabled(true);
        LOGGER.info("user registered");
       return userRepository.save(user);
    }

    /**
     * Method for update existing user
     *
     * @param existingUser   {@link User}
     * @param oAuth2UserInfo {@link OAuth2UserInfo}
     * @return user {@link User}
     */
    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFirstName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}




