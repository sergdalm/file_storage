package com.sergdalm.file_storage.repository;

import com.sergdalm.file_storage.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer> {
}
