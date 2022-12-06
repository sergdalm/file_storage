package com.sergdalm.file_storage.dto;

import com.sergdalm.file_storage.model.Role;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserReadDto {
    Integer id;
    String username;
    String email;
    Role role;
}
