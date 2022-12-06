package com.sergdalm.file_storage.repository;

import com.sergdalm.file_storage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String username);

    @Query("select u from File f join f.events e on e.type = 'UPLOAD' join e.user u where f.id =:fileId")
    Optional<User> findUserUploadedFile(Integer fileId);
}
