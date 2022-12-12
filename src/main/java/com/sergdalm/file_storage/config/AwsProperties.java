package com.sergdalm.file_storage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
public record AwsProperties(String endpointUrl,
                            String region,
                            String bucketName,
                            String accessKey,
                            String secretKey) {


}