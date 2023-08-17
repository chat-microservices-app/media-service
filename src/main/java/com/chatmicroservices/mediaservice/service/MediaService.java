package com.chatmicroservices.mediaservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public interface MediaService {
    public String uploadProfilePicture(MultipartFile file, String userId);

    public String uploadMedia(MultipartFile file);


    public void removeMedia(String location);


    public String updateMedia(MultipartFile file, String location);


    public String changeMediaLocation(String moveLocation, String currentLocation);

    public File createTempFile(MultipartFile file, String extension) throws IOException;

    public Map.Entry<String, String> getMediaExtension(String fileName);

}
