package com.sergdalm.file_storage.http.rest;

import com.sergdalm.file_storage.service.GenericService;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface ReadGenericRestController<ID, C, R, E> {

    <S extends GenericService<ID, C, R, E>> S getService();

    @GetMapping
    default List<R> findAll() {
        return getService().findAll();
    }

    R findById(ID id);
}
