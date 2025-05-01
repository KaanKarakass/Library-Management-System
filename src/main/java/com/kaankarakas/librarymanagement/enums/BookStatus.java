package com.kaankarakas.librarymanagement.enums;

public enum BookStatus {
    ACTIVE,
    BORROWED,
    DELETED;

    public static BookStatus getDefaultBookStatus() {
        return ACTIVE;
    }
}
