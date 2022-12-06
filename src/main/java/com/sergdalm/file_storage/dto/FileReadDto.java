package com.sergdalm.file_storage.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FileReadDto {
    Integer id;
    String name;
    Long size;
}
