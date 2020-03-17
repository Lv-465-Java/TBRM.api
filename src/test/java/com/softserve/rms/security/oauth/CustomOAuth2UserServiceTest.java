package com.softserve.rms.security.oauth;

import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.OAuth2AuthenticationProcessingException;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.security.oauth.user.OAuth2UserInfo;
import com.softserve.rms.security.oauth.user.OAuth2UserInfoFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CustomOAuth2UserService.class, OAuth2UserInfoFactory.class})
public class CustomOAuth2UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    OAuth2UserRequest oAuth2UserRequest;

    @Mock
    OAuth2UserInfo oAuth2UserInfo;

    @Mock
    OAuth2User oAuth2User;

    @Mock
    ClientRegistration clientRegistration=ClientRegistration.withRegistrationId("google")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .clientId("123")
            .redirectUriTemplate("redirectUri")
            .authorizationUri("authUri")
            .tokenUri("tokenUri")
            .build();

    @Mock
    OAuth2AccessToken oAuth2AccessToken=new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,"12334", Instant.MIN,Instant.MAX);

    @InjectMocks
    CustomOAuth2UserService customOAuth2UserService;

    private OAuth2UserRequest oAuth2UserRequest2=new OAuth2UserRequest(clientRegistration,oAuth2AccessToken);


    @Before
    public void init() {
        customOAuth2UserService = PowerMockito.spy(new CustomOAuth2UserService(userRepository));
    }

    @Test
    public void loadUserTest(){

    }

    @Test
    public void registerNewUserTest() throws Exception {
        when(oAuth2UserInfo.getName()).thenReturn("name");
        when(oAuth2UserInfo.getEmail()).thenReturn("email");
        when(oAuth2UserInfo.getImageUrl()).thenReturn("url");
        when(oAuth2UserInfo.getId()).thenReturn("1234");
        when(oAuth2UserRequest.getClientRegistration()).thenReturn(clientRegistration);
        User testUser=new User(oAuth2UserInfo.getName(),oAuth2UserInfo.getEmail(),new Role(5L, "ROLE_GUEST"),oAuth2UserInfo.getImageUrl(),oAuth2UserRequest.getClientRegistration().getRegistrationId(),oAuth2UserInfo.getId());
        when(userRepository.save(testUser)).thenReturn(testUser);
        Whitebox.invokeMethod(customOAuth2UserService, "registerNewUser",oAuth2UserRequest2,oAuth2UserInfo);
    }

    @Test
    public void updateExistingUserTest() throws Exception {
        User existUser=new User();
        when(oAuth2UserInfo.getName()).thenReturn("name");
        when(oAuth2UserInfo.getImageUrl()).thenReturn("url");
        when(userRepository.save(existUser)).thenReturn(existUser);
        Whitebox.invokeMethod(customOAuth2UserService, "updateExistingUser",existUser,oAuth2UserInfo);

    }

    @Test(expected = OAuth2AuthenticationProcessingException.class)
    public void processOAuth2UserTest() throws Exception {
        when(oAuth2UserRequest.getClientRegistration()).thenReturn(clientRegistration);
        Map<String, Object> attributes= Collections.emptyMap();
        when(oAuth2User.getAttributes()).thenReturn(attributes);
        spy(OAuth2UserInfoFactory.class);
        when(OAuth2UserInfoFactory.getOAuth2UserInfo(anyString(),Mockito.anyMap())).thenReturn(oAuth2UserInfo);
        when(oAuth2UserInfo.getEmail()).thenReturn(null);
        Whitebox.invokeMethod(customOAuth2UserService, "processOAuth2User",oAuth2UserRequest,oAuth2User);
    }
}
