package com.softserve.rms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Kravets Maryana
 */
@Configuration
@EnableWebSecurity
//@EnableSwagger2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private TokenManagementService tokenManagementService;

    public SecurityConfig(@Autowired TokenManagementService tokenManagementService){
        this.tokenManagementService=tokenManagementService;
    }

    public static final String REGISTER_URI="/register";
    public static final String LOGIN_URI="/authentication";

//    /**
//     *
//     * @return Docket
//     */
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("edu.softserve"))
//                .paths(PathSelectors.any())
//                .build();
//    }

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

        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
//        .authenticationEntryPoint(
//                (req,resp,e)->resp.sendError(HttpServletResponse.SC_UNAUTHORIZED)
//        )
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,REGISTER_URI,LOGIN_URI).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(new JwtAuthorizationFilter(tokenManagementService), UsernamePasswordAuthenticationFilter.class);
    }
}
