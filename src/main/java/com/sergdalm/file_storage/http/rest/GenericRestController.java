package com.sergdalm.file_storage.http.rest;

import org.springframework.http.ResponseEntity;

public interface GenericRestController<ID, C, R, J, E> extends ReadGenericRestController<ID, C, R, E> {

    R update(ID id, C createDto, J jwtAuthentication);

    ResponseEntity<?> delete(ID id, J jwtAuthentication);
}
