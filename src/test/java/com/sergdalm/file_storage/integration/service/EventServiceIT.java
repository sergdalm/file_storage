package com.sergdalm.file_storage.integration.service;

import com.sergdalm.file_storage.dto.EventCreateDto;
import com.sergdalm.file_storage.dto.EventReadDto;
import com.sergdalm.file_storage.integration.IntegrationTestBase;
import com.sergdalm.file_storage.model.Type;
import com.sergdalm.file_storage.service.EventService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AllArgsConstructor
public class EventServiceIT extends IntegrationTestBase {

    private final EventService eventService;

    @Test
    void findAll() {
        final List<EventReadDto> actualResult = eventService.findAll();
        assertThat(actualResult).hasSize(8);
    }

    @Test
    void findById() {
        final Optional<EventReadDto> actualResult = eventService.findById(1);
        assertThat(actualResult).isPresent();
        assertEquals(1, actualResult.get().getUserId());
        assertEquals(1, actualResult.get().getFileId());
        assertEquals(LocalDateTime.of(2022, 11, 22, 13, 4, 48), actualResult.get().getTime());
    }

    @Test
    void createEvent() {
        final EventCreateDto newEventDto = EventCreateDto.builder()
                .time(LocalDateTime.now())
                .type(Type.DOWNLOAD)
                .fileId(1)
                .userId(3)
                .build();

        final EventReadDto actualResult = eventService.create(newEventDto);

        assertEquals(newEventDto.getFileId(), actualResult.getFileId());
        assertEquals(newEventDto.getUserId(), actualResult.getUserId());
    }

    @Test
    void deleteEvent() {
        boolean actualResult = eventService.delete(1);
        final Optional<EventReadDto> actualUserDto = eventService.findById(1);
        assertTrue(actualResult);
        assertThat(actualUserDto).isEmpty();
    }
}
