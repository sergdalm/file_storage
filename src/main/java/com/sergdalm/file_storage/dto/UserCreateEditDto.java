package com.sergdalm.file_storage.dto;

import com.sergdalm.file_storage.model.Role;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
@Builder
public class UserCreateEditDto {

    @NotBlank
    @NotEmpty
    String username;

    @Email
    String email;

    @Size(min = 4, max = 128)
    String password;

    Role role;
}
