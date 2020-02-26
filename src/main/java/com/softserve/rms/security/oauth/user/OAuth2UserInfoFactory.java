package com.softserve.rms.security.oauth.user;

import com.softserve.rms.exceptions.OAuth2AuthenticationProcessingException;

import java.util.Map;

/**
 * class for getting user info from oauth2 provider
 * @author Kravets Maryana
 */
public class OAuth2UserInfoFactory {

    /**
     * method verify if user registered with oauth2 provider, and return info about user
     * @param registrationId {@link String}
     * @param attributes {@link Map}
     * @return OAuth2UserInfo
     */
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase("google")) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with google is not supported yet.");
        }
    }
}

