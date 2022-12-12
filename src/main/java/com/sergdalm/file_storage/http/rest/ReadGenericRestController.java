package com.sergdalm.file_storage.http.rest;

import com.sergdalm.file_storage.service.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface ReadGenericRestController<ID, C, R> {

    <S extends GenericService<ID, C, R, ?>> S getService();

    @GetMapping
    default List<R> findAll() {
        return getService().findAll();
    }

    @GetMapping("/{id}")
    default R findById(@PathVariable("id") ID id) {
        return getService().findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
