package com.chatmicroservices.mediaservice.config.security.filter;


import com.chatmicroservices.mediaservice.config.security.SecurityManager;
import com.chatmicroservices.mediaservice.config.api.RestProperties;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final SecurityManager securityManager;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException {

        //extract the token from the request header
        String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authToken != null && !authToken.isBlank() && authToken.startsWith(RestProperties.TOKEN_PREFIX)) {

            //remove the prefix from the token
            String tokenData = StringUtils.substringAfter(authToken, RestProperties.TOKEN_PREFIX);

            //authenticate the token
            securityManager.authenticate(tokenData).ifPresent(SecurityContextHolder.getContext()::setAuthentication);
        }
        // continue the filter chain
        filterChain.doFilter(request, response);

    }


}
