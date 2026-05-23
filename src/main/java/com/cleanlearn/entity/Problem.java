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
import lombok.Data;

@Entity
@Table(name = "problem", schema = "public")
@Data
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"problemId\"")
    private Long problemId;

    @Column(name = "\"sheetId\"", nullable = false)
    private Long sheetId;

    @Column(name = "\"problemDesc\"")
    private String problemDesc;

    @Column(name = "\"problemLink\"")
    private String problemLink;

    @Column(name = "\"createdBy\"")
    private String createdBy;

    @Column(name = "\"createdDate\"", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "\"updatedBy\"")
    private String updatedBy;

    @Column(name = "\"updatedDate\"", nullable = false)
    private LocalDateTime updatedDate;

    @Column(name = "level")
    private String level;

    @Column(name = "topic")
    private String topic;
}