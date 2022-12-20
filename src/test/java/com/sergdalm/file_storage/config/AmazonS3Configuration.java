package com.sergdalm.file_storage.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@TestConfiguration
public class AmazonS3Configuration {

    @Bean
    public AmazonS3 amazonS3(AwsProperties awsProperties) throws IOException {
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(awsProperties.endpointUrl(), awsProperties.region());
        final AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();
        amazonS3.createBucket(awsProperties.bucketName());
        Path filePath = Path.of("src", "test", "resources", "files", "text.txt");
        InputStream inputStream = Files.newInputStream(filePath, StandardOpenOption.READ);
        amazonS3.putObject(awsProperties.bucketName(), "text", inputStream, new ObjectMetadata());
        return amazonS3;
    }
}
