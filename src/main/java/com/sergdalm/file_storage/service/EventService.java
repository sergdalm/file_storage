package com.sergdalm.file_storage.service;

import com.sergdalm.file_storage.dto.EventCreateDto;
import com.sergdalm.file_storage.dto.EventReadDto;
import com.sergdalm.file_storage.mapper.CreateEditMapper;
import com.sergdalm.file_storage.mapper.ReadMapper;
import com.sergdalm.file_storage.model.Event;
import com.sergdalm.file_storage.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Getter
public class EventService implements GenericService<Integer, EventCreateDto, EventReadDto, Event> {

    private final EventRepository repository;
    private final CreateEditMapper<Event, EventCreateDto> createEditMapper;
    private final ReadMapper<Event, EventReadDto> readMapper;

    @Transactional
    @Override
    public EventReadDto create(EventCreateDto dto) {
        return Optional.of(dto)
                .map(getCreateEditMapper()::mapToEntity)
                .map(getRepository()::save)
                .map(getReadMapper()::mapToDto)
                .orElseThrow();
    }

    @Transactional
    @Override
    public Optional<EventReadDto> update(Integer id, EventCreateDto dto) {
        return getRepository().findById(id)
                .map(entity -> getCreateEditMapper().mapToEntity(dto, entity))
                .map(getRepository()::saveAndFlush)
                .map(getReadMapper()::mapToDto);
    }

    @Transactional
    @Override
    public boolean delete(Integer id) {
        return getRepository().findById(id)
                .map(entity -> {
                    getRepository().delete(entity);
                    getRepository().flush();
                    return true;
                })
                .orElse(false);
    }
}
