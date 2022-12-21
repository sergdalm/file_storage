package com.sergdalm.file_storage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-M-yyyy'T'hh:mm:ss")
    LocalDateTime time;
}
