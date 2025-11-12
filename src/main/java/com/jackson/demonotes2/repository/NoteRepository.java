package com.jackson.demonotes2.repository;

import com.jackson.demonotes2.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByContentContaining(String keyword);
}
