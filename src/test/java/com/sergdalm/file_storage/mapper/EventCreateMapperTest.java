package com.sergdalm.file_storage.mapper;

import com.sergdalm.file_storage.EntityUtil;
import com.sergdalm.file_storage.dto.EventCreateDto;
import com.sergdalm.file_storage.dto.UserCreateEditDto;
import com.sergdalm.file_storage.integration.IntegrationTestBase;
import com.sergdalm.file_storage.model.Event;
import com.sergdalm.file_storage.model.Type;
import com.sergdalm.file_storage.repository.FileRepository;
import com.sergdalm.file_storage.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class EventCreateMapperTest extends IntegrationTestBase {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EventCreateMapper eventCreateMapper;

    private final UserCreateEditDto newUserDto = UserCreateEditDto.builder()
            .username(EntityUtil.getUserWithoutId().getUsername())
            .email(EntityUtil.getUserWithoutId().getEmail())
            .password(EntityUtil.getUserWithoutId().getPassword())
            .role(EntityUtil.getUserWithoutId().getRole())
            .build();

    private final EventCreateDto eventDto = EventCreateDto.builder()
            .userId(EntityUtil.getUser().getId())
            .fileId(EntityUtil.getFile().getId())
            .type(EntityUtil.getEventWithoutId().getType())
            .time(EntityUtil.getEventWithoutId().getTime())
            .build();

    @Test
    void mapToEntityFromDto() {
        Event expectedEvent = EntityUtil.getEventWithoutId();
        expectedEvent.setFile(EntityUtil.getFile());
        expectedEvent.setUser(EntityUtil.getUser());

        // Mocks don't work
        Mockito.when(fileRepository.findById(eventDto.getFileId())).thenReturn(Optional.of(EntityUtil.getFile()));
        Mockito.when(userRepository.findById(eventDto.getUserId())).thenReturn(Optional.of(EntityUtil.getUser()));

        Event actualResult = eventCreateMapper.mapToEntity(eventDto);

        assertEquals(expectedEvent, actualResult);
        assertEquals(expectedEvent.getFile().getId(), actualResult.getFile().getId());
        assertEquals(expectedEvent.getUser().getId(), actualResult.getUser().getId());
    }

    @Test
    void mapToEntityFromDtoAndEntityWIthNoChanges() {
        Event expectedEvent = EntityUtil.getEvent();
        expectedEvent.setFile(EntityUtil.getFile());
        expectedEvent.setUser(EntityUtil.getUser());

        EventCreateDto eventDto = EventCreateDto.builder()
                .userId(50)
                .fileId(30)
                .type(Type.DOWNLOAD)
                .time(LocalDateTime.now())
                .build();
        Event actualResult = eventCreateMapper.mapToEntity(eventDto, expectedEvent);

        assertEquals(expectedEvent, actualResult);
        assertEquals(expectedEvent.getFile().getId(), actualResult.getFile().getId());
        assertEquals(expectedEvent.getUser().getId(), actualResult.getUser().getId());
    }
}