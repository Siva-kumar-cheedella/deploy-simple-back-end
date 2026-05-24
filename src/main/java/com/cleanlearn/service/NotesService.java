package com.cleanlearn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cleanlearn.repository.NotesRepository;
import com.cleanlearn.entity.Notes;
import java.time.LocalDateTime;

@Service
public class NotesService {

    @Autowired
    private NotesRepository notesRepository;

    public Long saveNotes(String noteDesc, Long userId, String userName) {
        
        Notes notes = new Notes();
        notes.setNoteDesc(noteDesc);
        notes.setUserId(userId);
        notes.setCreatedBy(userName);
        notes.setCreatedDate(LocalDateTime.now());
        notes.setUpdatedBy(userName);
        notes.setUpdatedDate(LocalDateTime.now());
        
        Notes savedNotes = notesRepository.save(notes);
        return savedNotes.getNoteId();

    }

    public void updateNotes(String notes, Long noteId, String updatedBy) {
        Notes existingNotes = notesRepository.findById(noteId)
            .orElseThrow(() -> new RuntimeException("Notes not found with id: " + noteId));

        existingNotes.setNoteDesc(notes);
        existingNotes.setUpdatedBy(updatedBy);
        existingNotes.setUpdatedDate(LocalDateTime.now());

        notesRepository.save(existingNotes);
    }
    
}

