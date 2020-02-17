package com.softserve.rms.security.oauth;
//
//import com.softserve.rms.entities.User;
//import com.softserve.rms.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//public class CustomOidcUserService extends OidcUserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
//        OidcUser oidcUser = super.loadUser(userRequest);
//        Map attributes = oidcUser.getAttributes();
//        GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo();
//        userInfo.setEmail((String) attributes.get("email"));
//        userInfo.setId((String) attributes.get("sub"));
//        userInfo.setImageUrl((String) attributes.get("picture"));
//        userInfo.setName((String) attributes.get("name"));
//        updateUser(userInfo);
//        return oidcUser;
//    }
//
//    private void updateUser(GoogleOAuth2UserInfo userInfo) {
//        User user = userRepository.findUserByEmail(userInfo.getEmail());
//        if(user == null) {
//            user = new User();
//        }
//        user.setEmail(userInfo.getEmail());
//        user.setImageUrl(userInfo.getImageUrl());
//        user.setName(userInfo.getName());
//        user.setUserType(UserType.google);
//        userRepository.save(user);
//    }
//}


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private OidcUserService oidcUserService;// = new OidcUserService();

//    @Autowired
//    public CustomOidcUserService(OidcUserService oidcUserService) {
//        this.oidcUserService = oidcUserService;
//    }


    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);
        return new CustomUserPrincipal(oidcUser);
    }
}


