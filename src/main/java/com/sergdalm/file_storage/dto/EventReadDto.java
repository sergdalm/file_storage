package com.sergdalm.file_storage.dto;

import com.sergdalm.file_storage.model.Type;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class EventReadDto {
    Integer id;
    Type type;
    Integer userId;
    Integer fileId;
    LocalDateTime time;
}
