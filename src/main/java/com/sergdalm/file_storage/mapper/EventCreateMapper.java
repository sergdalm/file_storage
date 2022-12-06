package com.sergdalm.file_storage.mapper;

import com.sergdalm.file_storage.dto.EventCreateDto;
import com.sergdalm.file_storage.model.Event;
import com.sergdalm.file_storage.repository.FileRepository;
import com.sergdalm.file_storage.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventCreateMapper implements CreateEditMapper<Event, EventCreateDto> {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Override
    public Event mapToEntity(EventCreateDto dto) {
        return Event.builder()
                .file(fileRepository.findById(dto.getFileId()).get())
                .user(userRepository.findById(dto.getUserId()).get())
                .type(dto.getType())
                .time(dto.getTime())
                .build();
    }

    // Event can't be changed on client's side
    @Override
    public Event mapToEntity(EventCreateDto dto, Event entity) {
        return entity;
    }
}
