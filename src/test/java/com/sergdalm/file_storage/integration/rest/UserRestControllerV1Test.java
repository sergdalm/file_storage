package com.sergdalm.file_storage.integration.rest;

import com.sergdalm.file_storage.dto.UserReadDto;
import com.sergdalm.file_storage.integration.IntegrationTestBase;
import com.sergdalm.file_storage.model.Role;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

@AllArgsConstructor
public class UserRestControllerV1Test extends IntegrationTestBase {

    @Autowired
    private final org.springframework.core.env.Environment environment;

    @Autowired
    private final TestRestTemplate restTemplate;

    private final String localhostUrl = "http://localhost:";

    @Test
    // This test doesn't work
    void findAll() throws Exception {
        String port = environment.getProperty("local.server.port");
        String mapping = "/users/1";
        UserReadDto userReadDto = UserReadDto.builder()
                .id(1)
                .username("sergdalm")
                .email("alex@gmail.com")
                .role(Role.ADMIN)
                .build();
        UserReadDto actualUser = restTemplate.getForObject(localhostUrl + port + mapping, UserReadDto.class);

        Assertions.assertEquals(userReadDto, actualUser);
    }
}
