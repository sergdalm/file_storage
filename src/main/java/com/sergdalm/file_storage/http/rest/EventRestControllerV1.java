package com.sergdalm.file_storage.http.rest;

import com.sergdalm.file_storage.dto.EventCreateDto;
import com.sergdalm.file_storage.dto.EventReadDto;
import com.sergdalm.file_storage.model.Event;
import com.sergdalm.file_storage.service.GenericService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Getter
public class EventRestControllerV1 implements ReadGenericRestController<Integer, EventCreateDto, EventReadDto, Event> {

    private final GenericService<Integer, EventCreateDto, EventReadDto, Event> service;

    @Override
    public EventReadDto findById(Integer id) {
        return getService().findById(id)
                .orElseThrow();
    }
}
