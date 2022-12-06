package com.sergdalm.file_storage.service;

import java.io.InputStream;
import java.util.Optional;

public interface GenericFileService<ID, C, R, E> extends GenericService<ID, C, R, E> {

    Optional<InputStream> downloadFile(ID fileId, ID userId);
}
