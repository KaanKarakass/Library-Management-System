package com.kaankarakas.librarymanagement.api.controller.book;

import com.kaankarakas.librarymanagement.api.constants.ApiEndpointConstants;
import com.kaankarakas.librarymanagement.dto.request.book.CreateBookRequest;
import com.kaankarakas.librarymanagement.dto.request.book.SearchBookRequest;
import com.kaankarakas.librarymanagement.dto.request.book.UpdateBookRequest;
import com.kaankarakas.librarymanagement.dto.response.book.BookDTO;
import com.kaankarakas.librarymanagement.dto.response.book.BookPageResponseDTO;
import com.kaankarakas.librarymanagement.service.book.BookCommandService;
import com.kaankarakas.librarymanagement.service.book.BookQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Book Controller", description = "Book management operations")
@RestController
@RequestMapping(value = ApiEndpointConstants.BOOK_API, produces = {ApiEndpointConstants.RESPONSE_CONTENT_TYPE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@RequiredArgsConstructor
@Slf4j
public class BookApiController {
    private final BookCommandService bookCommandService;
    private final BookQueryService bookQueryService;

    @PreAuthorize("hasAnyRole('LIBRARIAN')")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add book", description = "Add a new book to the library")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Book successfully added", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))), @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Invalid data\"}")))})
    public BookDTO addBook(@RequestBody CreateBookRequest request) {
        log.info("Adding new book with title: {}", request.getTitle());
        return bookCommandService.addBook(request);
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    @GetMapping(value = "/{id}", consumes = {MediaType.ALL_VALUE})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get book details by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Book found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))), @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Book not found with the given ID\"}")))})
    public BookDTO getBookById(@PathVariable Long id) {
        log.info("Retrieving book with id: {}", id);
        return bookQueryService.findBookById(id);
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    @GetMapping(value = "/search", consumes = {MediaType.ALL_VALUE})
    @Operation(summary = "Search books with filters and pagination")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Books retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookPageResponseDTO.class))), @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Invalid request parameters\"}"))), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Internal server error\"}")))})
    public BookPageResponseDTO searchBooks(SearchBookRequest request) {
        log.info("Searching books with criteria: {}", request);
        return bookQueryService.searchBooks(request);
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN')")
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an existing book")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Book successfully updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))), @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Book not found\"}"))), @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Invalid request body\"}")))})
    public BookDTO updateBook(@PathVariable Long id, @RequestBody @Valid UpdateBookRequest request) {
        log.info("Updating book id: {} with new data", id);
        return bookCommandService.updateBook(id, request);
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN')")
    @DeleteMapping(value = "delete/{id}", consumes = {MediaType.ALL_VALUE})
    @Operation(summary = "Soft delete a book")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Book successfully deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))), @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Book not found\"}"))), @ApiResponse(responseCode = "400", description = "Book already deleted", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Book already deleted\"}")))})
    public BookDTO deleteBook(@PathVariable Long id) {
        log.info("Deleting book with id: {}", id);
        return bookCommandService.deleteBook(id);
    }
}
