package com.sergdalm.file_storage;

import com.sergdalm.file_storage.model.Event;
import com.sergdalm.file_storage.model.File;
import com.sergdalm.file_storage.model.Role;
import com.sergdalm.file_storage.model.Type;
import com.sergdalm.file_storage.model.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class EntityUtil {

    public static User getUser() {
        return User.builder()
                .id(10)
                .email("test@gmail.com")
                .password("1234")
                .role(Role.USER)
                .username("test")
                .build();
    }

    public static User getUserWithoutId() {
        return User.builder()
                .email("test@gmail.com")
                .password("1234")
                .role(Role.USER)
                .username("test")
                .build();
    }

    public static File getFile() {
        return File.builder()
                .id(10)
                .name("image")
                .fileUrl("ulr")
                .size(10000L)
                .build();
    }

    public static File getFileWithoutId() {
        return File.builder()
                .name("image")
                .fileUrl("ulr")
                .size(10000L)
                .build();
    }

    public static Event getEvent() {
        return Event.builder()
                .id(20)
                .time(LocalDateTime.of(2022, 12, 6, 0, 0))
                .type(Type.UPLOAD)
                .build();
    }

    public static Event getEventWithoutId() {
        return Event.builder()
                .time(LocalDateTime.of(2022, 12, 6, 0, 0))
                .type(Type.UPLOAD)
                .build();
    }
}
