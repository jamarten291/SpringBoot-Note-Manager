package com.jackson.demonotes2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoteNotFoundException extends RuntimeException {
  public NoteNotFoundException(String message) {
    super(message);
  }

  public NoteNotFoundException(long id) {
    super("Nota no encontrada con id: " + id);
  }
}
