package com.sergdalm.file_storage.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Configuration {

    @Bean
    public AmazonS3 amazonS3(AwsProperties awsProperties) {
        AWSCredentials credentials = new BasicAWSCredentials(awsProperties.accessKey(), awsProperties.secretKey());
        return AmazonS3ClientBuilder.standard()
                .withRegion(awsProperties.region())
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
