package com.sergdalm.file_storage.http.rest;

import com.sergdalm.file_storage.dto.FileCreateEditDto;
import com.sergdalm.file_storage.dto.FileCreateEditDtoForRest;
import com.sergdalm.file_storage.dto.FileReadDto;
import com.sergdalm.file_storage.dto.UserReadDto;
import com.sergdalm.file_storage.dto.jwt.JwtAuthentication;
import com.sergdalm.file_storage.model.Role;
import com.sergdalm.file_storage.service.FileService;
import com.sergdalm.file_storage.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Getter
public class FileRestControllerV1 implements EditGenericRestController<Integer, FileCreateEditDtoForRest, FileReadDto, JwtAuthentication>,
        ReadGenericRestController<Integer, FileCreateEditDto, FileReadDto> {

    private final FileService service;
    private final UserService userService;

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") Integer id,
                                               JwtAuthentication jwtAuthentication) {
        return service.downloadFile(id, jwtAuthentication.getId())
                .map(content -> {
                    try {
                        return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                                .contentLength(content.available())
                                .body(content.readAllBytes());
                    } catch (IOException e) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                    }
                })
                .orElseGet(notFound()::build);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public FileReadDto create(String fileName,
                              MultipartFile fileContent,
                              JwtAuthentication jwtAuthentication) {
        return service.create(FileCreateEditDto.builder()
                .userId(jwtAuthentication.getId())
                .fileContent(fileContent)
                .fileName(fileName)
                .build());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public FileReadDto update(@PathVariable("id") Integer fileId,
                              @Validated @RequestBody FileCreateEditDtoForRest fileDto,
                              JwtAuthentication jwtAuthentication) {
        if (checkIfDoNotHaveAccess(fileId, jwtAuthentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return service.update(fileId, FileCreateEditDto.builder()
                        .userId(jwtAuthentication.getId())
                        .fileContent(fileDto.getFileContent())
                        .fileName(fileDto.getName())
                        .build())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> delete(@PathVariable("id") Integer fileId,
                                    JwtAuthentication jwtAuthentication) {
        if (checkIfDoNotHaveAccess(fileId, jwtAuthentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return service.delete(fileId)
                ? noContent().build()
                : notFound().build();
    }

    // This method checks if this file was not uploaded by the user or if it is not ADMIN
    private boolean checkIfDoNotHaveAccess(Integer fileId, JwtAuthentication jwtAuthentication) {
        if (jwtAuthentication.getAuthorities().contains(Role.ADMIN)) {
            return true;
        }
        return userService.findUserUploadedFile(fileId)
                .map(UserReadDto::getId)
                .map(userId -> Objects.equals(userId, jwtAuthentication.getId()))
                .orElse(false);
    }
}
