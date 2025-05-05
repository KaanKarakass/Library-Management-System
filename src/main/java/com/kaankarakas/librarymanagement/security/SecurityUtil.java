package com.kaankarakas.librarymanagement.security;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.kaankarakas.librarymanagement.validator.security.SecurityServiceValidationRule.ERR_UNAUTHENTICATED_ACCESS;
@Slf4j
public class SecurityUtil {
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("User successfully fetched from authentication context");
            return authentication.getName();
        }
        log.warn("Unauthenticated access attempt detected");
        throw new LibraryException(ERR_UNAUTHENTICATED_ACCESS.getDescription(), HttpStatus.UNAUTHORIZED);
    }
}
