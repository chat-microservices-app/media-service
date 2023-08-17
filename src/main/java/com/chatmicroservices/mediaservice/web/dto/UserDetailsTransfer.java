package com.chatmicroservices.mediaservice.web.dto;

import java.util.Set;

public record UserDetailsTransfer(
        String username,
        String password,
        Set<String> authorities,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        boolean enabled
) {
}
