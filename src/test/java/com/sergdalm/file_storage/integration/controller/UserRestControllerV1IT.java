package com.sergdalm.file_storage.integration.controller;

import com.sergdalm.file_storage.dto.UserCreateEditDto;
import com.sergdalm.file_storage.dto.UserLoginDto;
import com.sergdalm.file_storage.dto.UserReadDto;
import com.sergdalm.file_storage.dto.jwt.JwtAuthentication;
import com.sergdalm.file_storage.integration.IntegrationTestBase;
import com.sergdalm.file_storage.model.Role;
import com.sergdalm.file_storage.service.jwt.JwtProvider;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AllArgsConstructor
@AutoConfigureMockMvc
public class UserRestControllerV1IT extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtProvider jwtProvider;

    @Test
    void getUserById() throws Exception {
        UserReadDto expectedUserDto = UserReadDto.builder()
                .id(1)
                .username("sergdalm")
                .email("alex@gmail.com")
                .role(Role.ADMIN)
                .build();
        this.mockMvc.perform(get("/api/v1/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUserDto)));
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
                .id(4)
                .email(newUser.getEmail())
                .role(newUser.getRole())
                .username(newUser.getUsername())
                .build();
        this.mockMvc.perform(post("/api/v1/users")
                        .param("username", newUser.getUsername())
                        .param("email", newUser.getEmail())
                        .param("password", newUser.getPassword())
                        .param("role", newUser.getRole().name()))
                .andExpect(status().isCreated())
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
                .id(1)
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole())
                .username(updatedUser.getUsername())
                .build();
        this.mockMvc.perform(put("/api/v1/users/1")
                        .param("username", updatedUser.getUsername())
                        .param("email", updatedUser.getEmail())
                        .param("password", updatedUser.getPassword())
                        .param("role", updatedUser.getRole().name())
                        .header("Authorization", getJwtToken())
                        .principal(getJwtAuthentication()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUserReadDto)));
    }

    @Test
    void deleteUser() throws Exception {
        this.mockMvc.perform(delete("/api/v1/users/2")
                        .header("Authorization", getJwtToken())
                        .principal(getJwtAuthentication()))
                .andExpect(status().isNoContent());
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
