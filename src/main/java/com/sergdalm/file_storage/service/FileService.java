package com.sergdalm.file_storage.service;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.sergdalm.file_storage.dto.FileCreateEditDto;
import com.sergdalm.file_storage.dto.FileReadDto;
import com.sergdalm.file_storage.dto.FileUploadResult;
import com.sergdalm.file_storage.mapper.CreateEditMapper;
import com.sergdalm.file_storage.mapper.ReadMapper;
import com.sergdalm.file_storage.model.Event;
import com.sergdalm.file_storage.model.File;
import com.sergdalm.file_storage.model.Type;
import com.sergdalm.file_storage.repository.EventRepository;
import com.sergdalm.file_storage.repository.FileRepository;
import com.sergdalm.file_storage.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Getter
@Slf4j
public class FileService implements GenericService<Integer, FileCreateEditDto, FileReadDto, File> {

    private final FileRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final AmazonClient amazonClient;
    private final CreateEditMapper<File, FileCreateEditDto> createEditMapper;
    private final ReadMapper<File, FileReadDto> readMapper;
    private final String bucket = "";

    @Transactional
    @Override
    public FileReadDto create(FileCreateEditDto dto) {
        MultipartFile fileContent = dto.getFileContent();
        FileUploadResult fileUploadResult = amazonClient.uploadFile(dto.getFileName(), dto.getFileContent());
        if (!fileUploadResult.isUploadedResult()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        File fileEntity = File.builder()
                .name(dto.getFileName())
                .size(fileContent.getSize())
                .fileUrl(fileUploadResult.getFileUrl())
                .size(dto.getFileContent().getSize())
                .build();
        getRepository().save(fileEntity);
        Event event = Event.builder()
                .type(Type.UPLOAD)
                .time(LocalDateTime.now())
                .build();
        event.setFile(fileEntity);
        event.setUser(userRepository.findById(dto.getUserId()).orElseThrow());
        eventRepository.save(event);
        return readMapper.mapToDto(fileEntity);
    }

    @Transactional
    @Override
    public Optional<FileReadDto> update(Integer id, FileCreateEditDto dto) {
        return getRepository().findById(id)
                .map(entity -> {
                    amazonClient.deleteFileFromS3Bucket(entity.getFileUrl());
                    FileUploadResult fileUploadResult = amazonClient.uploadFile(dto.getFileName(), dto.getFileContent());
                    if (!fileUploadResult.isUploadedResult()) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    entity.setFileUrl(fileUploadResult.getFileUrl());
                    return getCreateEditMapper().mapToEntity(dto, entity);
                })
                .map(getRepository()::saveAndFlush)
                .map(getReadMapper()::mapToDto);
    }

    @Transactional
    @Override
    public boolean delete(Integer id) {
        return getRepository().findById(id)
                .map(entity -> {
                    amazonClient.deleteFileFromS3Bucket(entity.getFileUrl());
                    getRepository().delete(entity);
                    getRepository().flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public Optional<S3ObjectInputStream> downloadFile(Integer fileId, Integer userId) {
        return getRepository().findById(fileId)
                .map(file -> {
                    Event event = Event.builder()
                            .time(LocalDateTime.now())
                            .type(Type.DOWNLOAD)
                            .build();
                    event.setUser(userRepository.findById(userId).orElseThrow());
                    event.setFile(file);
                    eventRepository.save(event);
                    return file;
                })
                .map(File::getFileUrl)
                .filter(StringUtils::hasText)
                .map(amazonClient::downloadFromS3Bucket);  // ИСПРАВТЬ ЭТОТ МЕТОД
    }

    @SneakyThrows
    private Optional<InputStream> getContent(String fileName) {
        Path fileFullPath = Path.of(bucket, fileName);
        return Files.exists(fileFullPath)
                ? Optional.of(Files.newInputStream(fileFullPath))
                : Optional.empty();
    }
}
