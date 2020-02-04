package com.softserve.rms.security.filter;

import com.softserve.rms.security.TokenManagementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
public class JwtAuthorizationFilterTest {

        @Mock
        HttpServletRequest request;
        @Mock
        HttpServletResponse response;
        @Mock
        FilterChain chain;
        @Mock
        TokenManagementService tokenManagementService;
        @Mock
        AuthenticationManager authenticationManager;

        private JwtAuthorizationFilter jwtAuthorizationFilter;

        @Before
        public void setUp() {
            MockitoAnnotations.initMocks(this);
            jwtAuthorizationFilter = new JwtAuthorizationFilter(tokenManagementService);
        }

        @Test
        public void doFilterInternal() throws IOException, ServletException {
            when(tokenManagementService.resolveAccessToken(request)).thenReturn("SecretAccessToken");
            when(authenticationManager.authenticate(any()))
                    .thenReturn(new UsernamePasswordAuthenticationToken("Principal", null, Collections.emptyList()));
            doNothing().when(chain).doFilter(request, response);

            jwtAuthorizationFilter.doFilterInternal(request, response, chain);
           // verify(authenticationManager, times(1)).authenticate(any());
            verify(chain, times(1)).doFilter(any(), any());
        }
    }
