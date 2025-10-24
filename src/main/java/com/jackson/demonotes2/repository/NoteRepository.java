package com.jackson.demonotes2.repository;

import com.jackson.demonotes2.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByContentContaining(String keyword);
}
