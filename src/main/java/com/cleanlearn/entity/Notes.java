package com.cleanlearn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notes", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"noteId\"")
    private Long noteId;

    @Column(name = "\"noteDesc\"", nullable = false)
    private String noteDesc;

    @Column(name = "\"userId\"", nullable = false)
    private Long userId;

    @Column(name = "\"createdBy\"")
    private String createdBy;

    @Column(name = "\"createdDate\"")
    private LocalDateTime createdDate;

    @Column(name = "\"updatedBy\"")
    private String updatedBy;

    @Column(name = "\"updatedDate\"")
    private LocalDateTime updatedDate;
}