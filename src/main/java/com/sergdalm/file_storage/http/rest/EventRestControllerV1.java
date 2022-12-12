package com.sergdalm.file_storage.http.rest;

import com.sergdalm.file_storage.dto.EventCreateDto;
import com.sergdalm.file_storage.dto.EventReadDto;
import com.sergdalm.file_storage.service.EventService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Getter
public class EventRestControllerV1 implements ReadGenericRestController<Integer, EventCreateDto, EventReadDto> {

    private final EventService service;

}
