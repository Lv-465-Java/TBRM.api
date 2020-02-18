package com.softserve.rms.security.oauth;

import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOidcUserService extends OidcUserService {

    private UserRepository userRepository;

    @Autowired
    public CustomOidcUserService(UserRepository userRepository) {
        this.userRepository=userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Map attributes = oidcUser.getAttributes();
        GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(attributes);
        userInfo.setEmail((String) attributes.get("email"));
        userInfo.setId((String) attributes.get("sub"));
        userInfo.setImageUrl((String) attributes.get("picture"));
        userInfo.setName((String) attributes.get("name"));
        updateUser(userInfo);
        return oidcUser;
    }

    private void updateUser(GoogleOAuth2UserInfo userInfo) {
        User user = userRepository.findUserByEmail(userInfo.getEmail())
                .orElseThrow(()->new NotFoundException("not found"));
        if(user == null) {
            user = new User();
        }
        user.setEmail(userInfo.getEmail());
        //user.setImageUrl(userInfo.getImageUrl());
        user.setFirstName(userInfo.getName());
        //user.setUserType(UserType.google);
        user.setRole(new Role(5L, "ROLE_GUEST"));
        userRepository.save(user);
    }
}



