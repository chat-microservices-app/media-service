package com.chatmicroservices.mediaservice.config.security;


import com.chatmicroservices.mediaservice.config.security.client.SecurityServiceClient;
import com.chatmicroservices.mediaservice.web.dto.UserDetailsTransfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Log4j2
@RequiredArgsConstructor
@Component
public class SecurityManager {


    // to delegate authentication to the security service
    private final SecurityServiceClient securityServerClient;


    public Optional<Authentication> authenticate(final String authToken) {
        return getAuthenticationInfo(authToken).map(this::getAuthentication);
    }

    private Optional<UserDetailsTransfer> getAuthenticationInfo(String authToken) {
        try {
            return Optional.of(securityServerClient.checkToken(authToken));
        } catch (Exception exception) {
            log.error("Error while getting user details from security service", exception);
            return Optional.empty();
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(UserDetailsTransfer userDetailsTransfer) {
        Collection<? extends GrantedAuthority> authorities = ofNullable(userDetailsTransfer.authorities())
                .map(auth ->
                        auth.stream()
                                .map(perm -> (GrantedAuthority) new SimpleGrantedAuthority(perm))
                                .collect(Collectors.toSet())
                )
                .orElseGet(Set::of);
        return new UsernamePasswordAuthenticationToken(userDetailsTransfer, null, authorities);
    }
}
