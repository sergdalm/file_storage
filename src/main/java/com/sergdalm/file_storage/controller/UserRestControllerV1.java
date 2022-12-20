package com.sergdalm.file_storage.controller;

import com.sergdalm.file_storage.dto.UserCreateEditDto;
import com.sergdalm.file_storage.dto.UserReadDto;
import com.sergdalm.file_storage.dto.jwt.JwtAuthentication;
import com.sergdalm.file_storage.model.Role;
import com.sergdalm.file_storage.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@Getter
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestControllerV1 implements ReadGenericRestController<Integer, UserCreateEditDto, UserReadDto> {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserReadDto create(@Validated UserCreateEditDto user) {
        return service.create(user);
    }

    @PutMapping("/{id}")
    public UserReadDto update(@PathVariable("id") Integer id,
                              @Validated UserCreateEditDto user,
                              JwtAuthentication jwtAuthentication) {
        if (checkIfUserHaveAccess(id, jwtAuthentication)) {
            return service.update(id, user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                    JwtAuthentication jwtAuthentication) {
        if (checkIfUserHaveAccess(id, jwtAuthentication)) {
            return service.delete(id)
                    ? noContent().build()
                    : notFound().build();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    // This method check if this id does belong to the user or if the user has role ADMIN
    private boolean checkIfUserHaveAccess(Integer userId, JwtAuthentication jwtAuthentication) {
        return Objects.equals(jwtAuthentication.getId(), userId) || jwtAuthentication.getAuthorities().contains(Role.ADMIN);
    }
}
