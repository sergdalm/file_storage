package com.sergdalm.file_storage.integration.rest;

import com.sergdalm.file_storage.integration.IntegrationTestBase;
import com.sergdalm.file_storage.model.Role;
import io.findify.s3mock.S3Mock;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AllArgsConstructor
@AutoConfigureMockMvc
public class FileRestControllerIT extends IntegrationTestBase {

    private final static S3Mock api = new S3Mock.Builder().build();
    private final MockMvc mockMvc;

    @BeforeAll
    static void setUp() {
        api.start();
    }

    @Test
    void test() throws Exception {
        File file = new File("src/test/resources/files/text.txt");
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        this.mockMvc.perform(get("/api/v1/files/1/download").with(jwt().authorities(Role.ADMIN, Role.MODERATOR)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().bytes(fileBytes));
    }

    @AfterAll
    static void shutDown() {
        api.shutdown();
    }
}
