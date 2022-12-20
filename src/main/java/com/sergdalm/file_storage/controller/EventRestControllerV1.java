package com.sergdalm.file_storage.controller;

import com.sergdalm.file_storage.dto.EventCreateDto;
import com.sergdalm.file_storage.dto.EventReadDto;
import com.sergdalm.file_storage.service.EventService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Getter
public class EventRestControllerV1 implements ReadGenericRestController<Integer, EventCreateDto, EventReadDto> {

    private final EventService service;
}
