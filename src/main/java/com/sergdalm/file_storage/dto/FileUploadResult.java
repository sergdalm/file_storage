package com.sergdalm.file_storage.dto;

import lombok.Value;

@Value
public class FileUploadResult {
    String fileUrl;
    boolean uploadedResult;
}
