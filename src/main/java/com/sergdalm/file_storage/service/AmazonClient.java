package com.sergdalm.file_storage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.sergdalm.file_storage.config.AwsProperties;
import com.sergdalm.file_storage.dto.FileUploadResult;
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

    public File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File convFile;
        String fileName;
        if (!Objects.equals(multipartFile.getOriginalFilename(), "")) {
            fileName = multipartFile.getOriginalFilename();
            convFile = new File(Objects.requireNonNull(fileName));
            FileOutputStream outputStream = new FileOutputStream(convFile);
            outputStream.write(multipartFile.getBytes());
            outputStream.close();
        } else {
            // This code was wrote in order to make test run
            fileName = multipartFile.getName();
            convFile = new File(fileName);
            multipartFile.transferTo(convFile);
        }
        return convFile;
    }

    private String generateFileName(String fileName) {
        return fileName.replace(" ", "_") + LocalDateTime.now();
    }

    public void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(awsProperties.bucketName(), fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public FileUploadResult uploadFile(String fileName, MultipartFile multipartFile) {
        try {
            File file = convertMultiPartToFile(multipartFile);
            String generatedFileName = generateFileName(fileName);
            uploadFileTos3bucket(generatedFileName, file);
            file.delete();
            return new FileUploadResult(generatedFileName, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new FileUploadResult(null, false);
        }
    }

    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(awsProperties.bucketName(), fileName));
        return "Successfully deleted";
    }

    public S3ObjectInputStream downloadFromS3Bucket(String fileUrl) {
        return s3client.getObject(awsProperties.bucketName(), fileUrl).getObjectContent();
    }
}
