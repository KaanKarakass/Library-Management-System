package com.kaankarakas.librarymanagement.integration.base;

import com.kaankarakas.librarymanagement.dto.request.auth.AuthRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public abstract class BaseIntegrationTest {

    private static final String API_BASE_URL = "http://localhost:";
    private static final String AUTH_URL = "/api/auth/login";

    protected static String librarianToken;
    protected static String patronToken;

    @LocalServerPort
    private int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @BeforeEach
    void initTokens() {
        if (librarianToken == null || patronToken == null) {
            librarianToken = getToken(new TestRestTemplate(), port, "bob", "securePass");
            librarianToken = librarianToken.replace("{\"token\":\"", "").replace("\"}", "");

            patronToken = getToken(new TestRestTemplate(), port, "alice", "password123");
            patronToken = patronToken.replace("{\"token\":\"", "").replace("\"}", "");
        }
    }
    private static String getToken(TestRestTemplate restTemplate, int port, String username, String password) {
        AuthRequest loginRequest = new AuthRequest(username, password);
        ResponseEntity<String> response = restTemplate.postForEntity(
                API_BASE_URL + port + AUTH_URL,
                new HttpEntity<>(loginRequest),
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new RuntimeException("Failed to get token for " + username);
    }

    protected HttpHeaders jsonHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    protected String url(String path) {
        return API_BASE_URL + port + path;
    }
}
