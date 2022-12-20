package com.sergdalm.file_storage.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FileEditDto {
    Integer fileId;
    Integer userId;
    String newFileName;
}
