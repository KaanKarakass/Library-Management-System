package com.kaankarakas.librarymanagement.api.constants;

import org.springframework.http.MediaType;

public final class ApiEndpointConstants {
    public static final String RESPONSE_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";
    public static final String API_BASE_URL = "/api";
    public static final String BOOK_API = API_BASE_URL + "/books";
    public static final String USER_API = API_BASE_URL + "/users";
    public static final String AUTH_API = API_BASE_URL + "/auth";
    public static final String BORROW_API = API_BASE_URL + "/borrow-history";

}
