package com.sergdalm.file_storage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "events")
@EqualsAndHashCode(exclude = "events")
@Entity
@Table(name = "files")
public class File implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false, unique = true)
    String fileUrl;

    @Column(nullable = false)
    Long size;

    @Builder.Default
    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
    List<Event> events = new ArrayList<>();
}
