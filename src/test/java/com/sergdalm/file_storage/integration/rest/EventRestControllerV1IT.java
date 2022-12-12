package com.sergdalm.file_storage.integration.rest;

import com.sergdalm.file_storage.dto.EventReadDto;
import com.sergdalm.file_storage.integration.IntegrationTestBase;
import com.sergdalm.file_storage.model.Type;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AllArgsConstructor
public class EventRestControllerV1IT extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getEventById() throws Exception {
        final EventReadDto expectedEventDto = EventReadDto.builder()
                .id(1)
                .fileId(1)
                .userId(2)
                .time(LocalDateTime.of(2022, 11, 22, 13, 4, 48))
                .type(Type.UPLOAD)
                .build();
        this.mockMvc.perform(get("/api/v1/events/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedEventDto)));
    }

    @Test
    void getAllEvents() throws Exception {
        List<EventReadDto> expectedEvents = List.of(
                EventReadDto.builder()
                        .id(1)
                        .fileId(1)
                        .userId(2)
                        .time(LocalDateTime.of(2022, 11, 22, 13, 4, 48))
                        .type(Type.UPLOAD)
                        .build(),
                EventReadDto.builder()
                        .id(2)
                        .fileId(1)
                        .userId(3)
                        .time(LocalDateTime.of(2022, 11, 22, 13, 5, 50))
                        .type(Type.DOWNLOAD)
                        .build()
        );
        this.mockMvc.perform(get("/api/v1/events"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedEvents)));
    }
}
