package com.sergdalm.file_storage.mapper;

import com.sergdalm.file_storage.dto.FileCreateEditDto;
import com.sergdalm.file_storage.model.File;
import org.springframework.stereotype.Component;

@Component
public class FileCreateEditMapper implements CreateEditMapper<File, FileCreateEditDto> {

    @Override
    public File mapToEntity(FileCreateEditDto dto) {
        return File.builder()
                .name(dto.getFileName())
                .build();
    }

    @Override
    public File mapToEntity(FileCreateEditDto dto, File entity) {
        entity.setSize(dto.getFileContent().getSize());
        entity.setName(dto.getFileName());
        return entity;
    }
}
