package com.chatmicroservices.mediaservice.web;

import com.chatmicroservices.mediaservice.client.UserManagementClient;
import com.chatmicroservices.mediaservice.config.api.RestProperties;
import com.chatmicroservices.mediaservice.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;


@Log4j2
@RequiredArgsConstructor
@RequestMapping(RestProperties.ROOT + "/v1" + RestProperties.MEDIA.ROOT)
@RestController
public class MediaController {

    private final MediaService mediaService;
    private final UserManagementClient userManagementClient;


    // upload image
    @PostMapping(path = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                                       @PathVariable String userId,
                                                       @RequestHeader HttpHeaders headers) {
        URI location = URI.create(mediaService.uploadProfilePicture(file, userId));
        String token = Objects.requireNonNull(headers.get(HttpHeaders.AUTHORIZATION)).get(0);
        log.debug("location: {}", location.toString());
        // update the user profile picture
        userManagementClient.updateProfilePicture(
                userId,
                token,
                location.toString());
        return ResponseEntity.created(location).build();
    }

    // upload image for anything else
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadMedia(@RequestParam("file") MultipartFile file) {
        URI location = URI.create(mediaService.uploadMedia(file));
        return ResponseEntity.created(location).build();
    }


    @DeleteMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeMedia(@RequestParam("location") String location) {
        mediaService.removeMedia(location);
        return ResponseEntity.accepted().build();
    }


    // update the media
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateMedia(@RequestParam("file") MultipartFile file, @RequestParam("location") String location) {
        URI locate = URI.create(mediaService.updateMedia(file, location));
        return ResponseEntity.accepted().location(locate).build();
    }

    // change the media location
    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changeMediaLocation(@RequestParam("moveLocation") String moveLocation,
                                                      @RequestParam("currentLocation") String currentLocation) {
        URI location = URI.create(mediaService.changeMediaLocation(moveLocation, currentLocation));
        return ResponseEntity.accepted().location(location).build();
    }
}
