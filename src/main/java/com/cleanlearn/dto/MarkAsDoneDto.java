package com.cleanlearn.dto;

import lombok.Data;

@Data
public class MarkAsDoneDto {
    private Long userId;
    private String userName;
    private Long itemId;
    private String revision;
    private String notes;
}