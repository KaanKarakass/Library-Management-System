package com.kaankarakas.librarymanagement.api.controller.auth;

import com.kaankarakas.librarymanagement.api.constants.ApiEndpointConstants;
import com.kaankarakas.librarymanagement.dto.request.auth.AuthRequest;
import com.kaankarakas.librarymanagement.service.auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Controller", description = "Auth management operations")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiEndpointConstants.AUTH_API, produces = {ApiEndpointConstants.RESPONSE_CONTENT_TYPE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        log.info("Login request received for username: {}", request.username());
        return ResponseEntity.ok(authService.login(request));
    }
}
