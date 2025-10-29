package com.jackson.demonotes2.service;

import com.jackson.demonotes2.exception.ConcurrencyConflictException;
import com.jackson.demonotes2.exception.NoteNotFoundException;
import com.jackson.demonotes2.model.Note;
import com.jackson.demonotes2.model.NoteStats;
import com.jackson.demonotes2.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> findAll() {
        List<Note> notes = noteRepository.findAll();
        notes.sort((o1, o2) -> Long.compare(o1.getId(), o2.getId()));
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
        return noteRepository.save(note);
    }

    @Transactional
    public Note update(Long id, Note noteDetails) {
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
}