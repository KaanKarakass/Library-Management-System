package com.kaankarakas.librarymanagement.validator.security;

import lombok.Getter;

@Getter
public enum SecurityServiceValidationRule {
    ERR_INVALID_JWT("Invalid JWT token"),
    ERR_UNAUTHENTICATED_ACCESS("Unauthenticated access"),


    /*end of enum*/;

    private final String description;

    SecurityServiceValidationRule(String description){
        this.description = description;
    }
}
