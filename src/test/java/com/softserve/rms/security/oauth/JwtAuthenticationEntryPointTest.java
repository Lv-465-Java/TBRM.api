package com.softserve.rms.security.oauth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JwtAuthenticationEntryPoint.class)
public class JwtAuthenticationEntryPointTest {

    @Mock
    HttpServletRequest httpRequest;

    @Mock
    HttpServletResponse httpResponse;

    @Mock
    AuthenticationException authenticationException;

    @Test
    public void commenceTest() throws IOException {
        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint= PowerMockito.mock(JwtAuthenticationEntryPoint.class);
       doNothing().when(jwtAuthenticationEntryPoint).
                        commence(httpRequest,httpResponse,authenticationException);
    }
}
