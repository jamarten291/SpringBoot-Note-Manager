package com.jackson.demonotes2.controllers;

import com.jackson.demonotes2.exception.InvalidNoteContentException;
import com.jackson.demonotes2.model.Note;
import com.jackson.demonotes2.model.NoteStats;
import com.jackson.demonotes2.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        List<String> forbiddenWords = new ArrayList<>(List.of("spam", "anuncio", "publicidad"));

        if (containsForbiddenWord(note.getTitle(), forbiddenWords) ||
                containsForbiddenWord(note.getContent(), forbiddenWords)) {
            throw new InvalidNoteContentException();
        }

        Note created = noteService.create(note);
        return ResponseEntity.ok(created);
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
        List<String> forbiddenWords = List.of("spam", "publicidad", "anuncio");

        if (containsForbiddenWord(noteDetails.getTitle(), forbiddenWords) ||
                containsForbiddenWord(noteDetails.getContent(), forbiddenWords)) {
            throw new InvalidNoteContentException();
        }

        Note updated = noteService.update(id, noteDetails);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/stats")
    public NoteStats getNoteStats() {
        return noteService.getNoteStatistics();
    }

    private boolean containsForbiddenWord(String text, List<String> forbidden) {
        if (text == null) return false;
        String lower = text.toLowerCase().trim();
        // separar por no letras/dígitos para coincidir por palabra
        String[] tokens = lower.split("\\P{Alnum}+");
        for (String t : tokens) {
            if (forbidden.contains(t)) return true;
        }
        return false;
    }
}

