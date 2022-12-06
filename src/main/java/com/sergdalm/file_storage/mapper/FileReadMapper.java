package com.sergdalm.file_storage.mapper;

import com.sergdalm.file_storage.dto.FileReadDto;
import com.sergdalm.file_storage.model.File;
import org.springframework.stereotype.Component;

@Component
public class FileReadMapper implements ReadMapper<File, FileReadDto> {

    @Override
    public FileReadDto mapToDto(File entity) {
        return FileReadDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .size(entity.getSize())
                .build();
    }
}
