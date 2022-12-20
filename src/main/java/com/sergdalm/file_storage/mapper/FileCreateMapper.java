package com.sergdalm.file_storage.mapper;

import com.sergdalm.file_storage.dto.FileCreateDto;
import com.sergdalm.file_storage.model.File;
import org.springframework.stereotype.Component;

@Component
public class FileCreateMapper implements CreateEditMapper<File, FileCreateDto> {

    @Override
    public File mapToEntity(FileCreateDto dto) {
        return File.builder()
                .name(dto.getFileName())
                .build();
    }

    @Override
    public File mapToEntity(FileCreateDto dto, File entity) {
        entity.setSize(dto.getFileContent().getSize());
        entity.setName(dto.getFileName());
        return entity;
    }
}
