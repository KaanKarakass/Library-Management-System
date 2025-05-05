package com.kaankarakas.librarymanagement.security;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.kaankarakas.librarymanagement.validator.security.SecurityServiceValidationRule.ERR_UNAUTHENTICATED_ACCESS;

public class SecurityUtil {
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        throw new LibraryException(ERR_UNAUTHENTICATED_ACCESS.getDescription(), HttpStatus.UNAUTHORIZED);
    }
}
