package com.sergdalm.file_storage.http.rest;

import com.sergdalm.file_storage.dto.UserCreateEditDto;
import com.sergdalm.file_storage.dto.UserReadDto;
import com.sergdalm.file_storage.model.Role;
import com.sergdalm.file_storage.model.User;
import com.sergdalm.file_storage.service.GenericService;
import com.sergdalm.file_storage.service.jwt.JwtAuthentication;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@Getter
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestControllerV1 implements GenericRestController<Integer, UserCreateEditDto, UserReadDto, JwtAuthentication, User> {

    private final GenericService<Integer, UserCreateEditDto, UserReadDto, User> service;

    @GetMapping
    @Override
    public List<UserReadDto> findAll() {
        return service.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public UserReadDto findById(@PathVariable("id") Integer id) {
        return service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserReadDto create(@Validated @RequestBody UserCreateEditDto user) {
        return service.create(user);
    }

    @PutMapping("/{id}")
    @Override
    public UserReadDto update(@PathVariable("id") Integer id,
                              @Validated @RequestBody UserCreateEditDto user,
                              JwtAuthentication jwtAuthentication) {
        if (checkIfDoNotHaveAccess(id, jwtAuthentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return service.update(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                    JwtAuthentication jwtAuthentication) {
        if (checkIfDoNotHaveAccess(id, jwtAuthentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return service.delete(id)
                ? noContent().build()
                : notFound().build();
    }

    // This method check if this id does not belong to the user or if it is not ADMIN
    private boolean checkIfDoNotHaveAccess(Integer userId, JwtAuthentication jwtAuthentication) {
        return Objects.equals(jwtAuthentication.getId(), userId) || jwtAuthentication.getAuthorities().contains(Role.ADMIN);
    }
}
