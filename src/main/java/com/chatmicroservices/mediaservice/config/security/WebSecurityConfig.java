package com.chatmicroservices.mediaservice.config.security;


import com.chatmicroservices.mediaservice.config.api.RestProperties;
import com.chatmicroservices.mediaservice.config.security.filter.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
@EnableGlobalAuthentication
@Configuration
public class WebSecurityConfig {

    private final String SPRING_ACTUATOR_PATH = "/actuator";

    private final String ALLOW_ALL_ENDPOINTS = "/**";

    @Lazy
    private final JwtAuthFilter securityFilter;

    // set up basic security for all endpoints
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        // disable csrf since we are not passing html forms
        http.csrf(AbstractHttpConfigurer::disable);

        // login handler; remove it.
        http.formLogin(
                AbstractHttpConfigurer::disable
        ).httpBasic(
                AbstractHttpConfigurer::disable
        );

        // since we are using jwt, we don't need to use sessions
        // therefore we set the session management to stateless
        http.securityMatcher(RestProperties.ROOT + ALLOW_ALL_ENDPOINTS)
                .sessionManagement(
                        httpSecuritySessionManagementConfigurer ->
                                httpSecuritySessionManagementConfigurer
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                        .sessionAuthenticationFailureHandler(
                                                (request, response, exception) ->
                                                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                                        )
                )
                .authorizeHttpRequests(
                        amr ->
                                amr.requestMatchers(HttpMethod.OPTIONS).permitAll() // option methods are permitted by default
                                        .requestMatchers(allowedGetEndpoints()).permitAll() // get methods are permitted by default
                                        .anyRequest().authenticated() // all other requests are authenticated

                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class); // register the jwt filter before the username password filter
        return http.build();
    }


    // TODO: SETUP SWAGGER
    private String[] allowedGetEndpoints() {
        return new String[]{
                SPRING_ACTUATOR_PATH + ALLOW_ALL_ENDPOINTS,
                "/swagger-ui.html" + ALLOW_ALL_ENDPOINTS,
                "/swagger-ui" + ALLOW_ALL_ENDPOINTS,
                "/api-docs" + ALLOW_ALL_ENDPOINTS,
                "/error" + ALLOW_ALL_ENDPOINTS,
                "/error",
                "/h2-console" + ALLOW_ALL_ENDPOINTS
        };
    }
}
