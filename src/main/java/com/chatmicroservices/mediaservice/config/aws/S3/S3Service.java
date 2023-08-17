package com.chatmicroservices.mediaservice.config.aws.S3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class S3Service {


    private final AmazonS3 amazonS3Client;

    private final S3Buckets s3Buckets;

    public void putObject(String bucketName, String key, File content) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, content);
        amazonS3Client.putObject(putObjectRequest);

    }

    public String getUrl(String bucketName, String key) {

        return "https://" +
                s3Buckets.getMedia() + ".s3." +
                amazonS3Client.getRegionName() + ".amazonaws.com" +
        amazonS3Client.getUrl(bucketName, key).getPath();
    }


    public void deleteObject(String bucketName, String key) {
        amazonS3Client.deleteObject(bucketName, key);
    }


    public void moveObject(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey) {
        // Copy the object into a new object in the same bucket.
        amazonS3Client.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
        // Delete the original object.
        amazonS3Client.deleteObject(sourceBucketName, sourceKey);
    }


}
