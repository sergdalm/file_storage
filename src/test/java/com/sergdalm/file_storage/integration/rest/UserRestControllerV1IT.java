package com.sergdalm.file_storage.integration.rest;

import com.sergdalm.file_storage.dto.UserCreateEditDto;
import com.sergdalm.file_storage.dto.UserReadDto;
import com.sergdalm.file_storage.integration.IntegrationTestBase;
import com.sergdalm.file_storage.model.Role;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AllArgsConstructor
@AutoConfigureMockMvc
public class UserRestControllerV1IT extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getUserById() throws Exception {
        UserReadDto userReadDto = UserReadDto.builder()
                .id(1)
                .username("sergdalm")
                .email("alex@gmail.com")
                .role(Role.ADMIN)
                .build();
        this.mockMvc.perform(get("/api/v1/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userReadDto)));
    }

    @Test
    void getAllUser() throws Exception {
        List<UserReadDto> expectedUsers = List.of(
                UserReadDto.builder()
                        .id(1)
                        .username("sergdalm")
                        .email("alex@gmail.com")
                        .role(Role.ADMIN)
                        .build(),
                UserReadDto.builder()
                        .id(2)
                        .username("dimetros")
                        .email("dmitry@gmail.com")
                        .role(Role.MODERATOR)
                        .build(),
                UserReadDto.builder()
                        .id(3)
                        .username("luiza")
                        .email("luiza@gmail.com")
                        .role(Role.USER)
                        .build()
        );
        this.mockMvc.perform(get("/api/v1/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUsers)));
    }

    @Test
    void createUser() throws Exception {
        UserCreateEditDto newUser = UserCreateEditDto.builder()
                .username("username")
                .role(Role.USER)
                .email("user@gamil.com")
                .password("1234")
                .build();
        final UserReadDto expectedUserReadDto = UserReadDto.builder()
                .email(newUser.getEmail())
                .role(newUser.getRole())
                .username(newUser.getUsername())
                .build();
        this.mockMvc.perform(post("/api/v1/users").content(objectMapper.writeValueAsString(newUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUserReadDto)));
    }

    @Test
    void updateUser() throws Exception {
        UserCreateEditDto updatedUser = UserCreateEditDto.builder()
                .username("sergdalm")
                .role(Role.ADMIN)
                .email("alex111@gmail.com")
                .password("24r3u0")
                .build();
        final UserReadDto expectedUserReadDto = UserReadDto.builder()
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole())
                .username(updatedUser.getUsername())
                .build();
        this.mockMvc.perform(post("/api/v1/users").content(objectMapper.writeValueAsString(updatedUser)).content("1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUserReadDto)));
    }

    @Test
    void deleteUser() throws Exception {
        this.mockMvc.perform(delete("/api/v1/users").content("2"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
