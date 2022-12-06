package com.sergdalm.file_storage.mapper;

import com.sergdalm.file_storage.dto.EventReadDto;
import com.sergdalm.file_storage.model.Event;
import org.springframework.stereotype.Component;

@Component
public class EventReadMapper implements ReadMapper<Event, EventReadDto> {

    @Override
    public EventReadDto mapToDto(Event entity) {
        return EventReadDto.builder()
                .id(entity.getId())
                .fileId(entity.getFile().getId())
                .userId(entity.getUser().getId())
                .time(entity.getTime())
                .type(entity.getType())
                .build();
    }
}
