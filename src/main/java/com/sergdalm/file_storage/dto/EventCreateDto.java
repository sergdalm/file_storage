package com.sergdalm.file_storage.dto;

import com.sergdalm.file_storage.model.Type;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class EventCreateDto {
    Type type;
    Integer userId;
    Integer fileId;
    LocalDateTime time;
}
