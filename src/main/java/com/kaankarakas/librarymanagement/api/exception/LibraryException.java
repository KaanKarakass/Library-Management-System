package com.kaankarakas.librarymanagement.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LibraryException extends RuntimeException{
    private final HttpStatus status;

    public LibraryException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
