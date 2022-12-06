package com.sergdalm.file_storage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"user", "file"})
@EqualsAndHashCode(exclude = {"user", "file"})
@Entity
@Table(name = "events")
public class Event implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Type type;

    @Column(nullable = false)
    LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    File file;

    public void setUser(User user) {
        this.user = user;
        user.getEvents().add(this);
    }

    public void setFile(File file) {
        this.file = file;
        file.getEvents().add(this);
    }
}
