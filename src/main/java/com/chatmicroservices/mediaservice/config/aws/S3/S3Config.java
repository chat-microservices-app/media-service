package com.chatmicroservices.mediaservice.config.aws.S3;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Region;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "aws")
@Configuration
public class S3Config {

    private String region;

    @Bean
    public AmazonS3 amazonS3ClientBuilder() {
        String actualRegion = region == null ? Region.US_West.toString() : region;
        return AmazonS3ClientBuilder.standard().withRegion(actualRegion).build();
    }


}
