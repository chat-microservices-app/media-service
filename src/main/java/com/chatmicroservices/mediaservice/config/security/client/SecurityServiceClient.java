package com.chatmicroservices.mediaservice.config.security.client;


import com.chatmicroservices.mediaservice.config.api.RestProperties;
import com.chatmicroservices.mediaservice.web.dto.UserDetailsTransfer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// to communicate with auth-service and check if the token passed is valid
@FeignClient(name = "${security-service.name}")
public interface SecurityServiceClient {
    String BASE_URL = RestProperties.ROOT + "/v1" + RestProperties.AUTH.ROOT;

    @PostMapping(path = BASE_URL + RestProperties.AUTH.CHECK_TOKEN,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    UserDetailsTransfer checkToken(@RequestBody String token);

}
