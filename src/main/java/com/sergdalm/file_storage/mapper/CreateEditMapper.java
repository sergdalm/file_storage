package com.sergdalm.file_storage.mapper;

public interface CreateEditMapper<E, D> extends Mapper<E, D> {

    E mapToEntity(D dto);

    E mapToEntity(D dto, E entity);
}
