package com.kaankarakas.librarymanagement.enums;

public enum Status {
    ACTIVE,
    INACTIVE,
    DELETED;

    public static Status getDefaultStatus() {
        return ACTIVE;
    }
}
