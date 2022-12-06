package com.sergdalm.file_storage.integration.service;

import com.sergdalm.file_storage.dto.UserCreateEditDto;
import com.sergdalm.file_storage.dto.UserLoginDto;
import com.sergdalm.file_storage.dto.UserReadDto;
import com.sergdalm.file_storage.integration.IntegrationTestBase;
import com.sergdalm.file_storage.model.Role;
import com.sergdalm.file_storage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public class UserServiceIT extends IntegrationTestBase {

    private final UserService userService;

    @Test
    void findAllByFilter() {
        final List<UserReadDto> actualUsers = userService.findAll();
        assertThat(actualUsers).hasSize(6);
    }

    @Test
    void createUser() {
        UserCreateEditDto newUserDto = UserCreateEditDto
                .builder()
                .email("test@gmail.com")
                .username("test")
                .password("1234")
                .role(Role.USER)
                .build();
        UserReadDto expectedUserDto = UserReadDto.builder()
                .username(newUserDto.getUsername())
                .role(newUserDto.getRole())
                .email(newUserDto.getEmail())
                .build();

        UserReadDto actualUserDto = userService.create(newUserDto);

        assertEquals(expectedUserDto.getUsername(), actualUserDto.getUsername());
        assertEquals(expectedUserDto.getEmail(), actualUserDto.getEmail());
        assertEquals(expectedUserDto.getRole(), actualUserDto.getRole());
        assertThat(actualUserDto).isNotNull();
    }

    @Test
    void deleteUser() {
        boolean actualResult = userService.delete(1);
        final Optional<UserReadDto> actualUserDto = userService.findById(1);
        assertTrue(actualResult);
        assertThat(actualUserDto).isEmpty();
    }

    @Test
    void updateUser() {
        UserCreateEditDto editedUserDto = UserCreateEditDto
                .builder()
                .email("test@gmail.com")
                .username("test")
                .password("1234")
                .role(Role.USER)
                .build();

        Optional<UserReadDto> actualResult = userService.update(1, editedUserDto);

        assertThat(actualResult).isPresent();
        assertEquals(editedUserDto.getUsername(), actualResult.get().getUsername());
        assertEquals(editedUserDto.getEmail(), actualResult.get().getEmail());
        assertEquals(editedUserDto.getRole(), actualResult.get().getRole());
    }

    @Test
    void findByUploadedFileId() {
        final Optional<UserReadDto> actualResult = userService.findUserUploadedFile(3);

        assertThat(actualResult).isPresent();
        assertEquals("luiza@gmail.com", actualResult.get().getEmail());
    }

    @Test
    void findByUsername() {
        UserLoginDto actualResult = userService.loadUserByUsername("alex@gmail.com");

        assertEquals("sergdalm", actualResult.getUsername());
        assertEquals(1, actualResult.getId());
    }
}
