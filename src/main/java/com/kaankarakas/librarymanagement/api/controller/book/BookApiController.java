package com.kaankarakas.librarymanagement.api.controller.book;

import com.kaankarakas.librarymanagement.api.constants.ApiEndpointConstants;
import com.kaankarakas.librarymanagement.api.request.book.CreateBookRequest;
import com.kaankarakas.librarymanagement.dto.BookDTO;
import com.kaankarakas.librarymanagement.mapper.book.BookMapper;
import com.kaankarakas.librarymanagement.service.book.BookCommandService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = ApiEndpointConstants.BOOK_API, produces = {ApiEndpointConstants.RESPONSE_CONTENT_TYPE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@RequiredArgsConstructor
public class BookApiController {
    private final BookCommandService bookCommandService;
    private final BookMapper bookMapper;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add book", description = "Add a new book to the library")
    public BookDTO addBook(@RequestBody CreateBookRequest request) {
        return bookMapper.toDTO(bookCommandService.addBook(request));
    }
}
