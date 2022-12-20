package com.sergdalm.file_storage.service;

import com.sergdalm.file_storage.dto.UserCreateEditDto;
import com.sergdalm.file_storage.dto.UserLoginDto;
import com.sergdalm.file_storage.dto.UserReadDto;
import com.sergdalm.file_storage.mapper.CreateEditMapper;
import com.sergdalm.file_storage.mapper.ReadMapper;
import com.sergdalm.file_storage.model.User;
import com.sergdalm.file_storage.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Getter
public class UserService implements GenericService<Integer, UserCreateEditDto, UserReadDto, User>, UserDetailsService {

    private final UserRepository repository;
    private final FileService fileService;
    private final CreateEditMapper<User, UserCreateEditDto> createEditMapper;
    private final ReadMapper<User, UserReadDto> readMapper;

    @Transactional
    @Override
    public UserReadDto create(UserCreateEditDto userDto) {
        return Optional.of(userDto)
                .map(getCreateEditMapper()::mapToEntity)
                .map(getRepository()::save)
                .map(getReadMapper()::mapToDto)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Integer id, UserCreateEditDto userDto) {
        return getRepository().findById(id)
                .map(entity -> getCreateEditMapper().mapToEntity(userDto, entity))
                .map(getRepository()::saveAndFlush)
                .map(getReadMapper()::mapToDto);
    }

    @Transactional
    @Override
    public boolean delete(Integer id) {
        return getRepository().findById(id)
                .map(entity -> {
                    getRepository().delete(entity);
                    getRepository().flush();
                    return true;
                })
                .orElse(false);
    }

    public Optional<UserReadDto> findUserUploadedFile(Integer fileId) {
        return getRepository().findUserUploadedFile(fileId)
                .map(readMapper::mapToDto);
    }

    @Override
    public UserLoginDto loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .map(user -> new UserLoginDto(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole()),
                        user.getId()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user " + username));
    }
}
