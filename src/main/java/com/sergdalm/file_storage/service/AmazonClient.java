package com.sergdalm.file_storage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.sergdalm.file_storage.config.AwsProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class AmazonClient {

    private final AwsProperties awsProperties;
    private final Random random = new Random();
    private AmazonS3 s3client;

    @PostConstruct
    private void initializeAmazon() {

    }

    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(String fileName) {
        return fileName.replace(" ", "_") + LocalDateTime.now();
    }

    public void uploadFileTos3bucket(String fileName, File file) {
//        s3client.putObject(awsProperties.bucketName(), fileName, file);
        s3client.putObject(new PutObjectRequest(awsProperties.bucketName(), fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String uploadFile(String fileName, MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String generatedFileName = generateFileName(fileName);
            fileUrl = awsProperties.endpointUrl() + "/" + awsProperties.bucketName() + "/" + generatedFileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(awsProperties.bucketName() + "/", fileName));
        return "Successfully deleted";
    }

    public S3Object downloadFromS3Bucket(String fileName) {
        return s3client.getObject(awsProperties.bucketName(), fileName);
    }
}
