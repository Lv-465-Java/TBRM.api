package com.softserve.rms.security.oauth;

import com.softserve.rms.exceptions.OAuth2AuthenticationProcessingException;
import com.softserve.rms.security.oauth.user.GoogleOAuth2UserInfo;
import com.softserve.rms.security.oauth.user.OAuth2UserInfoFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Collections;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PrepareForTest(OAuth2UserInfoFactory.class)
public class OAuth2UserInfoFactoryTest {

    @Test(expected = OAuth2AuthenticationProcessingException.class)
    public void getOAuth2UserInfoFiledTest() {
        String registrationId = "google";
        Map<String, Object> attributes = Collections.emptyMap();
        GoogleOAuth2UserInfo googleOAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);
        spy(OAuth2UserInfoFactory.class);
        when(OAuth2UserInfoFactory.getOAuth2UserInfo(Mockito.anyString(), Mockito.anyMap())).thenReturn(googleOAuth2UserInfo);

        GoogleOAuth2UserInfo googleOAuth2UserInfoActual = (GoogleOAuth2UserInfo) OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
        PowerMockito.verifyStatic(OAuth2UserInfoFactory.class);

        assertEquals(googleOAuth2UserInfo, googleOAuth2UserInfoActual);
    }

    @Test
    public void getOAuth2UserInfoTest() {
        PowerMockito.mockStatic(OAuth2UserInfoFactory.class);
        String registrationId = "google";
        Map<String, Object> attributes = Collections.emptyMap();
        GoogleOAuth2UserInfo googleOAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);
        PowerMockito.when(OAuth2UserInfoFactory.getOAuth2UserInfo(eq("google"), Mockito.anyMap()))
                .thenReturn(googleOAuth2UserInfo);
        GoogleOAuth2UserInfo googleOAuth2UserInfoActual = (GoogleOAuth2UserInfo) OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);

        assertEquals(googleOAuth2UserInfo, googleOAuth2UserInfoActual);
    }
}
