package com.cleanlearn.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.cleanlearn.service.NotesService;
import org.springframework.web.bind.annotation.RequestParam;
import com.cleanlearn.dto.UpdateNotesRequest;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotesController {
    private final NotesService notesService;

    @PostMapping("/notes/{noteId}")
    public ResponseEntity<Object> addNotes(@PathVariable Long noteId, @RequestBody UpdateNotesRequest updateNotesRequest) {
        try {
            notesService.updateNotes(updateNotesRequest.getNotes(), noteId, updateNotesRequest.getUpdatedBy());
            return ResponseEntity.ok("Notes updated with ID: " + noteId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update notes: " + e.getMessage());
        }
    }
}