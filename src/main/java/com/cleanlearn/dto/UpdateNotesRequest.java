package com.cleanlearn.dto;

import lombok.Data;

@Data
public class UpdateNotesRequest {
    private String notes;
    private String updatedBy;
}