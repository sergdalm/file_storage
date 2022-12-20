package com.sergdalm.file_storage.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
@Builder
public class FileCreateDto {
    Integer userId;
    String fileName;
    MultipartFile fileContent;
}
