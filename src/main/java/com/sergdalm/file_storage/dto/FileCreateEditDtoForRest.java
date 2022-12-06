package com.sergdalm.file_storage.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class FileCreateEditDtoForRest {

    @NotEmpty
    @NotBlank
    String name;

    @NotNull
    MultipartFile fileContent;
}
