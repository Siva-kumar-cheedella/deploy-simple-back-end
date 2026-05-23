package com.cleanlearn.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "\"userItem\"")
@Data
public class UserItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"itemId\"")
    private Long itemId;

    @Column(name = "\"userId\"")
    private Long userId;

    @Column(name = "\"refId\"")
    private Long refId;

    @Column(name = "\"itemLink\"")
    private String itemLink;

    @Column(name = "\"itemType\"")
    private String itemType;

    @Column(name = "\"FA_date\"")
    private LocalDateTime faDate;

    @Column(name = "\"FA_Status\"")
    private String faStatus;

    @Column(name = "\"FA_Note_Id\"")
    private Long faNoteId;

    @Column(name = "\"R1_date\"")
    private LocalDateTime r1Date;

    @Column(name = "\"R1_Status\"")
    private String r1Status;

    @Column(name = "\"R1_Note_Id\"")
    private Long r1NoteId;

    @Column(name = "\"R2_date\"")
    private LocalDateTime r2Date;

    @Column(name = "\"R2_Status\"")
    private String r2Status;

    @Column(name = "\"R2_Note_Id\"")
    private Long r2NoteId;

    @Column(name = "\"createdBy\"")
    private String createdBy;

    @Column(name = "\"createdDate\"")
    private LocalDateTime createdDate;

    @Column(name = "\"updatedBy\"")
    private String updatedBy;

    @Column(name = "\"updatedDate\"")
    private LocalDateTime updatedDate;
}