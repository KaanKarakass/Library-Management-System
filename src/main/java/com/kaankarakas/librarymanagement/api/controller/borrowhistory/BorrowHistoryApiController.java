package com.kaankarakas.librarymanagement.api.controller.borrowhistory;

import com.kaankarakas.librarymanagement.api.constants.ApiEndpointConstants;
import com.kaankarakas.librarymanagement.dto.request.borrowhistory.BorrowRequestDTO;
import com.kaankarakas.librarymanagement.dto.request.borrowhistory.ReturnRequestDTO;
import com.kaankarakas.librarymanagement.dto.response.borrowhistory.BorrowHistoryDTO;
import com.kaankarakas.librarymanagement.service.borrowhistory.BorrowHistoryCommandService;
import com.kaankarakas.librarymanagement.service.borrowhistory.BorrowHistoryQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Borrow History Controller", description = "Borrow management operations")
@RestController
@RequestMapping(value = ApiEndpointConstants.BORROW_API, produces = {ApiEndpointConstants.RESPONSE_CONTENT_TYPE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@RequiredArgsConstructor
public class BorrowHistoryApiController {

    private final BorrowHistoryCommandService commandService;
    private final BorrowHistoryQueryService queryService;

    @PreAuthorize("hasRole('PATRON')")
    @PostMapping("/{userId}/borrow")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Borrow a book", description = "Allows a user to borrow a book if available and eligible", responses = {@ApiResponse(responseCode = "201", description = "Book successfully borrowed", content = @Content(schema = @Schema(implementation = BorrowHistoryDTO.class))), @ApiResponse(responseCode = "400", description = "Book is not available"), @ApiResponse(responseCode = "406", description = "User not eligible")})
    public BorrowHistoryDTO borrowBook(@PathVariable Long userId, @RequestBody BorrowRequestDTO request) {
        return commandService.borrowBook(userId, request);
    }

    @PreAuthorize("hasRole('PATRON')")
    @PostMapping("/{historyId}/return")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return a book", description = "Marks a borrowed book as returned and updates availability", responses = {@ApiResponse(responseCode = "200", description = "Book successfully returned", content = @Content(schema = @Schema(implementation = BorrowHistoryDTO.class))), @ApiResponse(responseCode = "404", description = "Borrow history not found or already returned")})
    public BorrowHistoryDTO returnBook(@PathVariable Long historyId, @RequestBody ReturnRequestDTO request) {
        return commandService.returnBook(historyId, request);
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get borrowing history for a user", description = "Returns the complete borrowing history for the given user ID", responses = {@ApiResponse(responseCode = "200", description = "Borrow history retrieved", content = @Content(schema = @Schema(implementation = BorrowHistoryDTO.class)))})
    public List<BorrowHistoryDTO> getUserBorrowHistory(@PathVariable Long userId) {
        return queryService.getUserHistory(userId);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all borrowing history", description = "Returns borrowing history for all users (for librarians)", responses = {@ApiResponse(responseCode = "200", description = "Borrow history retrieved", content = @Content(schema = @Schema(implementation = BorrowHistoryDTO.class)))})
    public List<BorrowHistoryDTO> getAllBorrowHistory() {
        return queryService.getAllHistories();
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/report/overdue")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Generate overdue books report", description = "Generates a report of all books that are overdue with user and book details", responses = {@ApiResponse(responseCode = "200", description = "Report successfully generated", content = @Content(mediaType = "text/plain"))})
    public String generateOverdueReport() {
        return queryService.generateOverdueReport();
    }
}
