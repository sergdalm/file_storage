package com.sergdalm.file_storage.mapper;

import com.sergdalm.file_storage.dto.UserCreateEditDto;
import com.sergdalm.file_storage.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreateEditMapper implements CreateEditMapper<User, UserCreateEditDto> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User mapToEntity(UserCreateEditDto dto) {
        User user = new User();
        return copy(dto, user);
    }

    @Override
    public User mapToEntity(UserCreateEditDto dto, User entity) {
        return copy(dto, entity);
    }

    private User copy(UserCreateEditDto userDto, User user) {
        user.setEmail(userDto.getEmail());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        return user;
    }
}
