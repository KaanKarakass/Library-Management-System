package com.kaankarakas.librarymanagement.api.controller.book;

import com.kaankarakas.librarymanagement.api.constants.ApiEndpointConstants;
import com.kaankarakas.librarymanagement.api.request.book.CreateBookRequest;
import com.kaankarakas.librarymanagement.api.request.book.SearchBookRequest;
import com.kaankarakas.librarymanagement.api.request.book.UpdateBookRequest;
import com.kaankarakas.librarymanagement.dto.BookDTO;
import com.kaankarakas.librarymanagement.dto.BookPageResponseDTO;
import com.kaankarakas.librarymanagement.mapper.book.BookMapper;
import com.kaankarakas.librarymanagement.service.book.BookCommandService;
import com.kaankarakas.librarymanagement.service.book.BookQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add book", description = "Add a new book to the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book successfully added",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Invalid data\"}")))
    })
    public BookDTO addBook(@RequestBody CreateBookRequest request) {
        return bookMapper.toDTO(bookCommandService.addBook(request));
    }

    @GetMapping(value = "/{id}", consumes = {MediaType.ALL_VALUE})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get book details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Book not found with the given ID\"}")))
    })
    public BookDTO getBookById(@PathVariable Long id) {
        return bookMapper.toDTO(bookQueryService.findBookById(id));
    }

    @GetMapping(value = "/search", consumes = {MediaType.ALL_VALUE})
    @Operation(summary = "Search books with filters and pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookPageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Invalid request parameters\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Internal server error\"}")))
    })
    public BookPageResponseDTO searchBooks(SearchBookRequest request) {
        return bookQueryService.searchBooks(request);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an existing book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Book not found\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Invalid request body\"}")))
    })
    public BookDTO updateBook(@RequestBody @Valid UpdateBookRequest request) {
        return bookMapper.toDTO(bookCommandService.updateBook(request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully deleted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Book not found\"}"))),
            @ApiResponse(responseCode = "400", description = "Book already deleted",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Book already deleted\"}")))
    })
    public BookDTO deleteBook(@PathVariable Long id) {
        return bookMapper.toDTO(bookCommandService.deleteBook(id));
    }
}
