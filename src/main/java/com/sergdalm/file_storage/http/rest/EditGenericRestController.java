package com.sergdalm.file_storage.http.rest;

import org.springframework.http.ResponseEntity;

public interface EditGenericRestController<ID, C, R, J> {

    R update(ID id, C createDto, J jwtAuthentication);

    ResponseEntity<?> delete(ID id, J jwtAuthentication);
}
