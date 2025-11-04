package com.jackson.demonotes2.controllers;

import com.jackson.demonotes2.exception.InvalidNoteContentException;
import com.jackson.demonotes2.model.Note;
import com.jackson.demonotes2.model.NoteStats;
import com.jackson.demonotes2.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@Validated
public class NoteRestController {

    private final NoteService noteService;

    public NoteRestController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<Note> findAll() {
        return noteService.findAll();
    }

    @PostMapping
    public ResponseEntity<Note> save(@Valid @RequestBody Note note) {
        List<String> forbiddenWords = List.of("spam", "publicidad", "anuncio");
        Note created = noteService.create(note);

        if (forbiddenWords.contains(created.getTitle()) ||
                forbiddenWords.contains(created.getContent())) {
            throw new InvalidNoteContentException();
        } else {
            return ResponseEntity.ok(created);
        }
    }

    @GetMapping("/{id}")
    public Note findOne(@PathVariable Long id) {
        return noteService.findByIdOrThrow(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @Valid @RequestBody Note noteDetails) {
        Note updated = noteService.update(id, noteDetails);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/stats")
    public NoteStats getNoteStats() {
        return noteService.getNoteStatistics();
    }
}

