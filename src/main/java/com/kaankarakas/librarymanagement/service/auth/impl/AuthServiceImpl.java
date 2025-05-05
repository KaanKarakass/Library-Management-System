package com.kaankarakas.librarymanagement.service.auth.impl;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.dto.request.auth.AuthRequest;
import com.kaankarakas.librarymanagement.dto.response.auth.AuthResponse;
import com.kaankarakas.librarymanagement.service.auth.CustomUserDetailsService;
import com.kaankarakas.librarymanagement.security.JwtUtil;
import com.kaankarakas.librarymanagement.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse login(AuthRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.username(), request.password());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        log.debug("Authentication successful for user: {}", request.username());

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtUtil.generateToken(userDetails);

        log.info("JWT generated for user: {}", request.username());

        return new AuthResponse(token);
    }

}
