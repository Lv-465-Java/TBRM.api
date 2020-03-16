package com.softserve.rms.security.service;

import com.softserve.rms.dto.JwtDto;
import com.softserve.rms.security.TokenManagementService;
import com.softserve.rms.security.UserPrincipalDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(value = MockitoJUnitRunner.class)
public class TokenManagementServiceTest {

    private final String expectedEmail = "test@gmail.com";
    private String secretKey="4C8kum4LxyKWYLM78sKdXrzbBjDCFyfX";
    private String token="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTU4MDgxNTY4MCwiZXhwIjoxNTg1OTk5NjgwfQ.OoS6q-qq2pfvBZoEVWZpTj8B7yPWN6stNf_D4SKmQck";

   @Mock
   HttpServletRequest request;

   @Mock
   UserPrincipalDetailsService userPrincipalDetailsService;

   @Mock
   UserDetails userDetails;

   @InjectMocks
   TokenManagementService tokenManagementService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(tokenManagementService, "expireTimeAccessToken", "100000");
        ReflectionTestUtils.setField(tokenManagementService, "expireTimeRefreshToken", "5184000000");
        ReflectionTestUtils.setField(tokenManagementService, "secretKey", "4C8kum4LxyKWYLM78sKdXrzbBjDCFyfX");
    }

    @Test
    public void generateTokenPairTest() {
        JwtDto jwtDto = tokenManagementService.generateTokenPair(expectedEmail);

        String actualEmail =  Jwts.parser()
                .setSigningKey(TextCodec.BASE64.decode(secretKey))
                .parseClaimsJws(jwtDto.getAccessToken())
                .getBody()
                .getSubject();
        assertEquals(expectedEmail, actualEmail);

        assertNotNull(jwtDto);

        String actualEmail2 =  Jwts.parser()
                .setSigningKey(TextCodec.BASE64.decode(secretKey))
                .parseClaimsJws(jwtDto.getRefreshToken())
                .getBody()
                .getSubject();
        assertEquals(expectedEmail, actualEmail2);
    }

    @Test
    public void refreshTokenTest() {
        JwtDto jwtDto = tokenManagementService.refreshTokens("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTU4MDgxNTY4MCwiZXhwIjoxNTg1OTk5NjgwfQ.OoS6q-qq2pfvBZoEVWZpTj8B7yPWN6stNf_D4SKmQck");
        assertNotNull(jwtDto);
    }

    @Test
    public void getUserEmailTest() {
        String actualEmail = tokenManagementService.getUserEmail(
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTU4MDgxNTY4MCwiZXhwIjoxNTg1OTk5NjgwfQ.OoS6q-qq2pfvBZoEVWZpTj8B7yPWN6stNf_D4SKmQck");
        assertEquals(expectedEmail, actualEmail);
    }

    @Test

    public void validateTokenTest() {
        boolean valid = tokenManagementService.validateToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYWFAdWtyLm5ldCIsImlhdCI6MTU4MDg5NzQ5NywiZXhwIjoxNTg2MDgxNDk3fQ.NxL2kXGRkZJe3uHvf1nL5tYSK0kLjT15hFdLfn8OU3M");
        Assert.assertTrue(valid);
    }

    @Test
    public void getAccessTokenFromHttpServletRequest() {
        final String expectedToken = "An AccessToken";
        when(request.getHeader("authorization")).thenReturn("Bearer " + expectedToken);
        String actualToken = tokenManagementService.resolveAccessToken(request);
        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void emptyAccessTokenTest() {
        when(request.getHeader("authorization")).thenReturn(null);
        String actualToken = tokenManagementService.resolveAccessToken(request);
        assertNull(actualToken);
    }

    @Test
    public void getRefreshTokenFromHttpServletRequest() {
        final String expectedToken = "An RefreshToken";
        when(request.getHeader("refreshToken")).thenReturn(expectedToken);
        String actualToken = tokenManagementService.resolveRefreshToken(request);
        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void emptyRefreshTokenTest() {
        when(request.getHeader("refreshToken")).thenReturn(null);
        String actualToken = tokenManagementService.resolveRefreshToken(request);
        assertNull(actualToken);
    }

    @Test
    public void getAuthenticationTest(){
        when(userPrincipalDetailsService.loadUserByUsername(expectedEmail)).thenReturn(userDetails);
        assertEquals(tokenManagementService.getAuthentication(token), new UsernamePasswordAuthenticationToken(userDetails,
                "", userDetails.getAuthorities()));
    }
}
