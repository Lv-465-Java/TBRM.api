package com.softserve.rms.security.config;

import com.softserve.rms.security.TokenManagementService;
import com.softserve.rms.security.filter.JwtAuthorizationFilter;
import com.softserve.rms.security.oauth.CustomAuthenticationSuccessHandler;
import com.softserve.rms.security.oauth.CustomOidcUserService;
import com.softserve.rms.security.oauth.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Config for security
 * @author Kravets Maryana
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//        securedEnabled = true,
//        jsr250Enabled = true,
//        prePostEnabled = true
//)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private TokenManagementService tokenManagementService;
    private CustomOidcUserService customOidcUserService;
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    /**
     * constructor
     * @param tokenManagementService {@link TokenManagementService}
     */
    @Autowired
    public WebSecurityConfig(TokenManagementService tokenManagementService,
                             CustomOidcUserService customOidcUserService,
                             CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                             JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint){
        this.tokenManagementService=tokenManagementService;
        this.customOidcUserService=customOidcUserService;
        this.customAuthenticationSuccessHandler=customAuthenticationSuccessHandler;
        this.unauthorizedHandler=jwtAuthenticationEntryPoint;
    }



    private static final String[] AUTH_WHITELIST = {
            "/registration",
            "/authentication",
            "/refresh"
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
                .loginPage("/login/oauth2")
                .defaultSuccessUrl("/",true)
                .failureUrl("/oauth_login")
                .permitAll()
           // .and()
//                .logout()
//                    .logoutUrl("/")
//                    .logoutSuccessUrl("/oauth_login").permitAll()
        .redirectionEndpoint()
        .baseUri("/oauth2/callback/*")
        .and()
        .userInfoEndpoint()
        .oidcUserService(customOidcUserService)
                .and()
        .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(customAuthorizationRequestRepository())
        .and()
        .successHandler(customAuthenticationSuccessHandler);

                http
                .addFilterBefore(new JwtAuthorizationFilter(tokenManagementService), UsernamePasswordAuthenticationFilter.class);


    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthorizationRequestRepository customAuthorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    /**
     * Method configures urls
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web)  {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedOrigin("*");
        configuration.setAllowCredentials(true);
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("RefreshToken");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
