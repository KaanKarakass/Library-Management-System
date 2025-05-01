package com.kaankarakas.librarymanagement.enums;

public enum UserStatus {
    ACTIVE,
    DELETED;

    public static UserStatus getDefaultUserStatus() {
        return ACTIVE;
    }
}
