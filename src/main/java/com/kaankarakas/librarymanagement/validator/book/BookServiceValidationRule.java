package com.kaankarakas.librarymanagement.validator.book;

import lombok.Getter;

@Getter
public enum BookServiceValidationRule{

    ERR_BOOK_ALREADY_EXISTS("A book with this ISBN already exists"),
    ERR_PUBLICATION_DATE_FUTURE("Publication date cannot be in the future."),
    ERR_BOOK_NOT_FOUND("Book not found with the given ID"),
    ERR_BOOK_ALREADY_DELETED("book is already deleted."),


    /*end of enum*/;

    private final String description;

    BookServiceValidationRule(String description){
        this.description = description;
    }

}
