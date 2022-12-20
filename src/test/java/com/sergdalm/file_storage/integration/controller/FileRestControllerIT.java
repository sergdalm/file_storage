package com.sergdalm.file_storage.integration.controller;

import com.sergdalm.file_storage.dto.FileReadDto;
import com.sergdalm.file_storage.dto.UserLoginDto;
import com.sergdalm.file_storage.dto.jwt.JwtAuthentication;
import com.sergdalm.file_storage.integration.IntegrationTestBase;
import com.sergdalm.file_storage.model.Role;
import com.sergdalm.file_storage.service.jwt.JwtProvider;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AllArgsConstructor
@AutoConfigureMockMvc
public class FileRestControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebApplicationContext webApplicationContext;

    @Test
    void findAll() throws Exception {
        final List<FileReadDto> expectedList = List.of(FileReadDto.builder()
                .id(1)
                .name("text")
                .size(5076L)
                .build());
        this.mockMvc.perform(get("/api/v1/files")
                        .header("Authorization", getAdminJwtToken())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedList)));
    }

    @Test
    void findById() throws Exception {
        final FileReadDto expectedFileDto = FileReadDto.builder()
                .id(1)
                .name("text")
                .size(5076L)
                .build();
        this.mockMvc.perform(get("/api/v1/files/1")
                        .header("Authorization", getAdminJwtToken())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedFileDto)));
    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "test", authorities = {"USER"})
    void downloadFile() throws Exception {
        File file = new File("src/test/resources/files/text.txt");
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        this.mockMvc.perform(get("/api/v1/files/1/download")
                        .header("Authorization", getAdminJwtToken())
                )
                .andExpect(status().isOk())
                .andExpect(content().bytes(fileBytes));
    }

    @Test
    void uploadFile() throws Exception {
        File file = new File("src/test/resources/files/text.txt");
        String newFileName = "new file";
        FileReadDto expectedFile = FileReadDto.builder()
                .id(2)
                .size(file.length())
                .name(newFileName)
                .build();
        MockMvc mockMvcForUpload = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvcForUpload.perform(MockMvcRequestBuilders.multipart("/api/v1/files")
                        .file("fileContent", Files.readAllBytes(file.toPath()))
                        .param("fileName", newFileName)
                        .header("Authorization", getAdminJwtToken())
                        .principal(getAdminJwtAuthentication())
                )
                .andExpect(status().is(201))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedFile)));
    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "test", authorities = {"USER"})
    void shouldReturnForbiddenStatusWhenUploadingFileByUser() throws Exception {
        File file = new File("src/test/resources/files/text.txt");
        String newFileName = "new file";
        MockMvc mockMvcForUpload = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvcForUpload.perform(MockMvcRequestBuilders.multipart("/api/v1/files")
                        .file("fileContent", Files.readAllBytes(file.toPath()))
                        .param("fileName", newFileName)
                        .header("Authorization", getUserJwtToken())
                        .principal(getUserJwtAuthentication())
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        File file = new File("src/test/resources/files/text.txt");
        String newFileName = "super file";
        FileReadDto expectedFile = FileReadDto.builder()
                .id(1)
                .size(file.length())
                .name(newFileName)
                .build();

        this.mockMvc.perform(put("/api/v1/files/1")
                        .header("Authorization", getAdminJwtToken())
                        .param("newFileName", newFileName)
                        .param("fileId", "1")
                        .principal(getAdminJwtAuthentication())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedFile)));
    }

    @Test
    void deleteFile() throws Exception {
        this.mockMvc.perform(delete("/api/v1/files/1")
                        .header("Authorization", getAdminJwtToken())
                        .param("fileId", "1")
                        .principal(getAdminJwtAuthentication()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnForbiddenStatusWhenDeletingFileByByModeratorWhoDidNotCreateThisFile() throws Exception {
        this.mockMvc.perform(delete("/api/v1/files/1")
                        .header("Authorization", getModeratorJwtToken())
                        .param("fileId", "1")
                        .principal(getModeratorJwtAuthentication()))
                .andExpect(status().isForbidden());
    }

    private UserLoginDto getAdminLoginDto() {
        return new UserLoginDto("alex@gmail.com", "123", List.of(Role.ADMIN), 1);
    }

    private JwtAuthentication getAdminJwtAuthentication() {
        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setFirstName(getAdminLoginDto().getUsername());
        jwtAuthentication.setId(getAdminLoginDto().getId());
        jwtAuthentication.setAuthenticated(true);
        jwtAuthentication.setFirstName("Test");
        return jwtAuthentication;
    }

    private String getAdminJwtToken() {
        return "Bearer " + jwtProvider.generateAccessToken(
                getAdminLoginDto());
    }

    private UserLoginDto getUserLoginDto() {
        return new UserLoginDto("luiza@gmail.com", "23f3f", List.of(Role.USER), 3);
    }

    private JwtAuthentication getUserJwtAuthentication() {
        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setFirstName(getUserLoginDto().getUsername());
        jwtAuthentication.setId(getUserLoginDto().getId());
        jwtAuthentication.setAuthenticated(true);
        jwtAuthentication.setFirstName("Test");
        return jwtAuthentication;
    }

    private String getUserJwtToken() {
        return "Bearer " + jwtProvider.generateAccessToken(
                getModeratorLoginDto());
    }

    private UserLoginDto getModeratorLoginDto() {
        return new UserLoginDto("dmitry@gmail.com", "18393", List.of(Role.MODERATOR), 2);
    }

    private JwtAuthentication getModeratorJwtAuthentication() {
        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setFirstName(getModeratorLoginDto().getUsername());
        jwtAuthentication.setId(getModeratorLoginDto().getId());
        jwtAuthentication.setAuthenticated(true);
        jwtAuthentication.setFirstName("Test");
        return jwtAuthentication;
    }

    private String getModeratorJwtToken() {
        return "Bearer " + jwtProvider.generateAccessToken(
                getModeratorLoginDto());
    }
}
