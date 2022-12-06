package com.sergdalm.file_storage.mapper;

public interface ReadMapper<E, D> extends Mapper<E, D> {

    D mapToDto(E entity);
}
