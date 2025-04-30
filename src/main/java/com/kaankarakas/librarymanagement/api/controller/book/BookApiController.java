package com.kaankarakas.librarymanagement.api.controller.book;

import com.kaankarakas.librarymanagement.api.constants.ApiEndpointConstants;
import com.kaankarakas.librarymanagement.api.request.book.CreateBookRequest;
import com.kaankarakas.librarymanagement.api.request.book.SearchBookRequest;
import com.kaankarakas.librarymanagement.dto.BookDTO;
import com.kaankarakas.librarymanagement.dto.BookPageResponseDTO;
import com.kaankarakas.librarymanagement.mapper.book.BookMapper;
import com.kaankarakas.librarymanagement.service.book.BookCommandService;
import com.kaankarakas.librarymanagement.service.book.BookQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = ApiEndpointConstants.BOOK_API, produces = {ApiEndpointConstants.RESPONSE_CONTENT_TYPE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@RequiredArgsConstructor
public class BookApiController {
    private final BookCommandService bookCommandService;
    private final BookQueryService bookQueryService;
    private final BookMapper bookMapper;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add book", description = "Add a new book to the library")
    public BookDTO addBook(@RequestBody CreateBookRequest request) {
        return bookMapper.toDTO(bookCommandService.addBook(request));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get book details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public BookDTO getBookById(@PathVariable UUID id) {
        return bookMapper.toDTO(bookQueryService.findBookById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search books with filters and pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public BookPageResponseDTO searchBooks(SearchBookRequest request){
        return bookQueryService.searchBooks(request);
    }
}
