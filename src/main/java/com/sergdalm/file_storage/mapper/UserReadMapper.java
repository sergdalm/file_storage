package com.sergdalm.file_storage.mapper;

import com.sergdalm.file_storage.dto.UserReadDto;
import com.sergdalm.file_storage.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements ReadMapper<User, UserReadDto> {

    @Override
    public UserReadDto mapToDto(User entity) {
        return UserReadDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .role(entity.getRole())
                .build();
    }
}
