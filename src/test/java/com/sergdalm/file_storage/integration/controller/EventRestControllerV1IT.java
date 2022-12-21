package com.sergdalm.file_storage.integration.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sergdalm.file_storage.dto.EventReadDto;
import com.sergdalm.file_storage.dto.UserLoginDto;
import com.sergdalm.file_storage.dto.jwt.JwtAuthentication;
import com.sergdalm.file_storage.integration.IntegrationTestBase;
import com.sergdalm.file_storage.model.Role;
import com.sergdalm.file_storage.model.Type;
import com.sergdalm.file_storage.service.jwt.JwtProvider;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AllArgsConstructor
@AutoConfigureMockMvc
public class EventRestControllerV1IT extends IntegrationTestBase {

    private final MockMvc mockMvc;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtProvider jwtProvider;

    @Test
    void getEventById() throws Exception {
        final EventReadDto expectedEventDto = EventReadDto.builder()
                .id(1)
                .fileId(1)
                .userId(1)
                .time(LocalDateTime.of(2022, 11, 22, 13, 4, 48))
                .type(Type.UPLOAD)
                .build();
        this.mockMvc.perform(get("/api/v1/events/1")
                        .principal(getJwtAuthentication())
                        .param("Authorization", getJwtToken()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedEventDto)));
    }

    //EventReadDto(id=1, type=UPLOAD, userId=1, fileId=1, time=2022-11-22T13:04:48)
    @Test
    void getUserById() throws Exception {
        EventReadDto expectedEventDto = EventReadDto.builder()
                .id(1)
                .type(Type.UPLOAD)
                .userId(1)
                .fileId(1)
                .time(LocalDateTime.of(2022, 11, 22, 13, 4, 48))
                .build();


        this.mockMvc.perform(get("/api/v1/events/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedEventDto)));
//               .andExpect(content().string("""
//                {"id":1,"type":"UPLOAD","userId":1,"fileId":1,"time":"22-11-2022T01:04:48"}"""));
    }

    @Test
    void getAllEvents() throws Exception {
        List<EventReadDto> expectedEvents = List.of(
                EventReadDto.builder()
                        .id(2)
                        .fileId(1)
                        .userId(3)
                        .time(LocalDateTime.of(2022, 11, 22, 13, 5, 50))
                        .type(Type.DOWNLOAD)
                        .build(),
                EventReadDto.builder()
                        .id(1)
                        .fileId(1)
                        .userId(1)
                        .time(LocalDateTime.of(2022, 11, 22, 13, 4, 48))
                        .type(Type.UPLOAD)
                        .build()
        );
        this.mockMvc.perform(get("/api/v1/events")
                        .principal(getJwtAuthentication())
                        .param("Authorization", getJwtToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedEvents)));
    }

    @Test
    void getAllUser() throws Exception {
        List<EventReadDto> expectedEvents = List.of(
                EventReadDto.builder()
                        .id(2)
                        .fileId(1)
                        .userId(3)
                        .time(LocalDateTime.of(2022, 11, 22, 13, 5, 50))
                        .type(Type.DOWNLOAD)
                        .build(),
                EventReadDto.builder()
                        .id(1)
                        .fileId(1)
                        .userId(1)
                        .time(LocalDateTime.of(2022, 11, 22, 13, 4, 48))
                        .type(Type.UPLOAD)
                        .build()
        );
        this.mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedEvents)));
    }


    private JwtAuthentication getJwtAuthentication() {
        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setFirstName(getUserLoginDto().getUsername());
        jwtAuthentication.setId(getUserLoginDto().getId());
        jwtAuthentication.setAuthenticated(true);
        jwtAuthentication.setFirstName("Test");
        return jwtAuthentication;
    }

    private UserLoginDto getUserLoginDto() {
        return new UserLoginDto("alex@gmail.com", "123", List.of(Role.ADMIN), 1);
    }

    private String getJwtToken() {
        return "Bearer " + jwtProvider.generateAccessToken(
                new UserLoginDto(getUserLoginDto().getUsername(), getUserLoginDto().getPassword(), getUserLoginDto().getAuthorities(), getUserLoginDto().getId()));
    }
}
