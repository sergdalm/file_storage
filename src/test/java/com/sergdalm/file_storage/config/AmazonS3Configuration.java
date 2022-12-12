package com.sergdalm.file_storage.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.File;

@TestConfiguration
public class AmazonS3Configuration {

    @Bean
    public AmazonS3 amazonS3(AwsProperties awsProperties) {
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(awsProperties.endpointUrl(), awsProperties.region());
        final AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();
        amazonS3.createBucket(awsProperties.bucketName());
        File file = new File("src/test/resources/files/text.txt");
        amazonS3.putObject(awsProperties.bucketName(), "text", file);
        return amazonS3;
    }
}
