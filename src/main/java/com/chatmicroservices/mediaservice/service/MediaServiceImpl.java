package com.chatmicroservices.mediaservice.service;

import com.chatmicroservices.mediaservice.config.aws.S3.S3Buckets;
import com.chatmicroservices.mediaservice.config.aws.S3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class MediaServiceImpl implements MediaService {

    private final S3Service s3Service;
    private final S3Buckets s3Buckets;

    @Override
    public String uploadProfilePicture(MultipartFile file, String username) {
        Map.Entry<String, String> mediaInfo = getMediaExtension(Objects.requireNonNull(file.getOriginalFilename()));
        String location = "profile-pictures/" + username + "/" + mediaInfo.getValue() + "_" + UUID.randomUUID() + mediaInfo.getKey();

        try {
            File temp = createTempFile(file, mediaInfo.getKey());

            s3Service.putObject(s3Buckets.getMedia(), location, temp);

            // delete temp file
            temp.delete();

            return s3Service.getUrl(s3Buckets.getMedia(), location);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading profile picture", e);
        }
    }

    @Override
    public String uploadMedia(MultipartFile file) {
        Map.Entry<String, String> mediaInfo = getMediaExtension(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            String location = mediaInfo.getValue() + "_" + UUID.randomUUID() + mediaInfo.getKey();
            File temp = createTempFile(file, mediaInfo.getKey());

            s3Service.putObject(s3Buckets.getMedia(), location, temp);

            temp.delete();
            return s3Service.getUrl(s3Buckets.getMedia(), location);
        } catch (IOException ignored) {
            throw new RuntimeException("Error uploading media");
        }
    }

    @Override
    public void removeMedia(String location) {
        try {
            s3Service.deleteObject(s3Buckets.getMedia(), location);
        } catch (Exception ignored) {
            throw new RuntimeException("Error removing media");
        }
    }

    @Override
    public String updateMedia(MultipartFile file, String location) {
        try {
            Map.Entry<String, String> mediaInfo = getMediaExtension(Objects.requireNonNull(file.getOriginalFilename()));
            location += "/"+ mediaInfo.getValue() + UUID.randomUUID() + mediaInfo.getKey() ;
            File temp = createTempFile(file, mediaInfo.getKey());
            s3Service.putObject(s3Buckets.getMedia(), location, file.getResource().getFile());
            return s3Service.getUrl(s3Buckets.getMedia(), location);
        } catch (IOException ignored) {
            throw new RuntimeException("Error updating media");
        }

    }

    @Override
    public String changeMediaLocation(String moveLocation, String currentLocation) {
        try {
            s3Service.moveObject(s3Buckets.getMedia(), currentLocation, s3Buckets.getMedia(), moveLocation);
            return s3Service.getUrl(s3Buckets.getMedia(), moveLocation);
        } catch (Exception ignored) {
            throw new RuntimeException("Error changing media location");
        }

    }

    @Override
    public File createTempFile(MultipartFile file, String extension) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            // create a temp file
            File temp = File.createTempFile("temp", extension);
            // write it
            Files.copy(inputStream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return temp;
        } catch (IOException e) {
            throw new IOException("Error creating temp file", e);
        }
    }

    // extension for key and name for value
    @Override
    public Map.Entry<String, String> getMediaExtension(String fileName) {
        String extension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
        String name = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
        return Map.entry(extension, name);
    }
}
