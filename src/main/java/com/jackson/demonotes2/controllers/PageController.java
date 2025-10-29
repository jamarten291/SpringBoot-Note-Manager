package com.jackson.demonotes2.controllers;


import java.util.List;

import com.jackson.demonotes2.exception.ConcurrencyConflictException;
import com.jackson.demonotes2.exception.NoteNotFoundException;
import com.jackson.demonotes2.model.NoteStats;
import com.jackson.demonotes2.repository.NoteRepository;
import com.jackson.demonotes2.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired; // Asegúrate de importar el repositorio
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.jackson.demonotes2.model.Note;

@Controller
public class PageController {

    private final NoteService noteService;

    public PageController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/menu")
    public String showMenu() {
        return "menu";
    }

    @GetMapping("/new-note")
    public String showNewNoteForm(Model model) {
        model.addAttribute("note", new Note());
        return "new_note";
    }

    @PostMapping("/create-note")
    public String createNote(@Valid Note note, BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("note", note);
            return "new_note";
        }
        noteService.create(note);
        return "redirect:/list-notes";
    }

    @GetMapping("/list-notes")
    public String showAllNotes(Model model) {
        List<Note> notes = noteService.findAll();
        model.addAttribute("notes", notes);
        return "list_notes";
    }

    @GetMapping("/delete-note")
    public String showDeleteNotePage(Model model) {
        return "delete_note";
    }

    @GetMapping("/edit-note/{id}")
    public String showEditNoteForm(@PathVariable Long id, Model model) {
        Note note = noteService.findByIdOrThrow(id);
        model.addAttribute("note", note);
        return "edit_note";
    }

    @PutMapping("/edit-note/{id}")
    public String updateNoteMvc(@PathVariable Long id, @Valid Note note, BindingResult br, Model model) {
//        if (br.hasErrors()) {
//            model.addAttribute("note", note);
//            return "edit_note";
//        }
        noteService.update(id, note);
        return "redirect:/list-notes";
    }

    @GetMapping("/important-note")
    public String showImportantNote(Model model) {
        List<Note> notes = noteService.findByContentContaining("importante");
        model.addAttribute("notes", notes);
        return "list_notes";
    }

    @GetMapping("/search-note")
    public String showSearchNote(Model model) {
        return "search_note";
    }

    @GetMapping("/find-note")
    public String showSearchResult(@RequestParam("keyword") String keyword, Model model) {
        List<Note> notes = noteService.findByContentContaining(keyword);
        model.addAttribute("notes", notes);
        return "list_notes";
    }

    @GetMapping("/stats")
    public NoteStats getNoteStats() {
        return noteService.getNoteStatistics();
    }

    @GetMapping("/test-500")
    public String triggerInternalError() {
        String s = null;
        s.length();
        return "menu";
    }
}

