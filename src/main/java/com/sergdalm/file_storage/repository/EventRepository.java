package com.sergdalm.file_storage.repository;

import com.sergdalm.file_storage.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {

}
