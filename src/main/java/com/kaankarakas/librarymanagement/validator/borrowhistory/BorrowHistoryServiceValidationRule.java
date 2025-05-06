package com.kaankarakas.librarymanagement.validator.borrowhistory;

import lombok.Getter;

@Getter
public enum BorrowHistoryServiceValidationRule {
    ERR_USER_NOT_ELIGIBLE("User is not eligible to borrow"),
    ERR_BOOK_NOT_AVAILABLE("Book is not available"),
    ERR_BORROW_RECORD_NOT_FOUND("Borrow record not found"),
    ERR_BOOK_ALREADY_RETURNED("Book already returned"),
    ERR_INVALID_USER("You are not allowed to return books for other users"),
    /*end of enum*/;

    private final String description;

    BorrowHistoryServiceValidationRule(String description){
        this.description = description;
    }
}
