package com.softserve.rms.security.config;

import com.softserve.rms.security.TokenManagementService;
import com.softserve.rms.security.UserPrincipalDetailsService;
import com.softserve.rms.security.filter.JwtAuthorizationFilter;
import com.softserve.rms.security.oauth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletResponse;

import java.util.Locale;

/**
 * Config for security
 *
 * @author Kravets Maryana
 */
@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private TokenManagementService tokenManagementService;
    private CustomOAuth2UserService customOAuth2UserService;
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    private UserPrincipalDetailsService userPrincipalDetailsService;
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;


    /**
     * constructor
     *
     * @param tokenManagementService {@link TokenManagementService}
     */
    @Autowired
    public WebSecurityConfig(TokenManagementService tokenManagementService,
                             CustomOAuth2UserService customOAuth2UserService,
                             OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                             OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler,
                             HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                             JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                             UserPrincipalDetailsService userPrincipalDetailsService) {
        this.tokenManagementService = tokenManagementService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.unauthorizedHandler = jwtAuthenticationEntryPoint;
        this.userPrincipalDetailsService = userPrincipalDetailsService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    private static final String[] AUTH_WHITELIST = {
            "/registration",
            "/authentication",
            "/refresh",
            "/reset_password",
            "/forgot_password"

    };

    /**
     * Provides AuthenticationManager.
     *
     * @return {@link AuthenticationManager}
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * method configure patterns to define protected/unprotected API endpoints
     *
     * @param http {@link HttpSecurity}
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/permission/**").hasRole("MANAGER")
                .antMatchers(AUTH_WHITELIST)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);
        http
                .addFilterBefore(new JwtAuthorizationFilter(tokenManagementService), UsernamePasswordAuthenticationFilter.class);


    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userPrincipalDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * Method configures urls
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        return localeResolver;
    }

    /**
     * Method configures cors
     *
     * @return
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedOrigin("*");
        configuration.setAllowCredentials(true);
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("authorization");
        configuration.addExposedHeader("refreshToken");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
