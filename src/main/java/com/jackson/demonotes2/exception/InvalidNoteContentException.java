package com.jackson.demonotes2.exception;

public class InvalidNoteContentException extends RuntimeException {
    public InvalidNoteContentException() {
        super("Error al agregar la nota ya que su contenido no es válido: ");
    }
}
