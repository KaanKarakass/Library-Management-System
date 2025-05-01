package com.kaankarakas.librarymanagement.validator.user;

import lombok.Getter;

@Getter
public enum UserServiceValidationRule {
    ERR_EMAIL_ALREADY_EXISTS("Email already in use"),
    ERR_USERNAME_ALREADY_EXISTS("Username already in use"),
    ERR_INVALID_PASSWORD("Password must be at least 8 characters long"),
    ERR_INVALID_ROLE("Invalid user role"),
    ERR_USER_NOT_FOUND("User not found"),
    ERR_USER_ALREADY_DELETED("User is already deleted")
    /*end of enum*/;

    private final String description;

    UserServiceValidationRule(String description){
        this.description = description;
    }
}
