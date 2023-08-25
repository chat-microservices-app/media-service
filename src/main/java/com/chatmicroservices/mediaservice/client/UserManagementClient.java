package com.chatmicroservices.mediaservice.client;

import com.chatmicroservices.mediaservice.config.api.RestProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${user-management-service.name}", url = "${user-management-service.url}")
public interface UserManagementClient {

    String BASE_URL = RestProperties.ROOT + "/v1" + RestProperties.USER.ROOT;


    @PutMapping(path = BASE_URL + "/{userId}" + RestProperties.USER.UPDATE_PROFILE_PICTURE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateProfilePicture(@PathVariable(name = "userId") String userId,
                              @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                              @RequestHeader(HttpHeaders.LOCATION) String location);


}
