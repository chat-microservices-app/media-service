package com.chatmicroservices.mediaservice.config.aws.S3;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter @Setter
@ConfigurationProperties(prefix = "aws.s3.buckets")
@Component
public class S3Buckets {

    private String media;


}
