package com.sergdalm.file_storage.service;

import com.sergdalm.file_storage.mapper.CreateEditMapper;
import com.sergdalm.file_storage.mapper.ReadMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GenericService<ID, C, R, E> {

    JpaRepository<E, ID> getRepository();

    <M extends ReadMapper<E, R>> M getReadMapper();

    <N extends CreateEditMapper<E, C>> N getCreateEditMapper();

    default List<R> findAll() {
        return getRepository().findAll().stream()
                .map(getReadMapper()::mapToDto)
                .toList();
    }

    default Optional<R> findById(ID id) {
        return getRepository().findById(id)
                .map(getReadMapper()::mapToDto);
    }

    R create(C dto);

    boolean delete(ID id);
}
