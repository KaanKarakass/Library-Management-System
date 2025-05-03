package com.kaankarakas.librarymanagement.service.auth;

import com.kaankarakas.librarymanagement.dto.request.auth.AuthRequest;
import com.kaankarakas.librarymanagement.dto.response.auth.AuthResponse;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AuthService {
    AuthResponse login(AuthRequest request);
}
