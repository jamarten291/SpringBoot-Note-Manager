package com.jackson.demonotes2.service;

import com.jackson.demonotes2.exception.ConcurrencyConflictException;
import com.jackson.demonotes2.exception.InvalidNoteContentException;
import com.jackson.demonotes2.exception.NoteNotFoundException;
import com.jackson.demonotes2.model.Note;
import com.jackson.demonotes2.model.NoteStats;
import com.jackson.demonotes2.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> findAll() {
        List<Note> notes = noteRepository.findAll();
        notes.sort(Comparator.comparingInt(n -> n.getCreationDate().getNano()));
        return notes;
    }

    public List<Note> findAllSortedByContent() {
        List<Note> notes = noteRepository.findAll();
        notes.sort(Comparator.comparingInt(n -> n.getContent().length()));
        return notes;
    }

    public Note findByIdOrThrow(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));
    }

    public NoteStats getNoteStatistics() {
        List<Note> notes = noteRepository.findAll();
        return new NoteStats(
                notes.size(),
                // Promedio del contenido de las notas, si no lo encuentra devuelve 0
                notes.stream()
                        .mapToDouble(n -> n.getContent().length())
                        .average()
                        .orElse(0),
                // Nota de mayor longitud de título, si no lo encuentra devuelve 0
                notes.stream()
                        .mapToInt(n -> n.getTitle().length())
                        .max()
                        .orElse(0)
        );
    }

    @Transactional
    public Note create(Note note) {
        List<String> forbiddenWords = new ArrayList<>(List.of("spam", "anuncio", "publicidad"));

        if (containsForbiddenWord(note.getTitle(), forbiddenWords) ||
                containsForbiddenWord(note.getContent(), forbiddenWords) ||
                note.getContent().length() < 10) {
            throw new InvalidNoteContentException();
        }
        if (note.getContent().contains("DUPLICADO")) {
            throw new ConcurrencyConflictException(note.getContent());
        }

        return noteRepository.save(note);
    }

    @Transactional
    public Note update(Long id, Note noteDetails) {
        List<String> forbiddenWords = new ArrayList<>(List.of("spam", "anuncio", "publicidad"));

        if (containsForbiddenWord(noteDetails.getTitle(), forbiddenWords) ||
                containsForbiddenWord(noteDetails.getContent(), forbiddenWords)) {
            throw new InvalidNoteContentException();
        }

        Note note = findByIdOrThrow(id);

        if ("CONFLICTO".equalsIgnoreCase(noteDetails.getContent())) {
            throw new ConcurrencyConflictException("Nota ID " + id);
        }

        note.setTitle(noteDetails.getTitle());
        note.setContent(noteDetails.getContent());

        return noteRepository.save(note);
    }

    @Transactional
    public void delete(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        noteRepository.deleteById(id);
    }

    public List<Note> findByContentContaining(String keyword) {
        return noteRepository.findByContentContaining(keyword);
    }

    // Method used to validate the content of the new note
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