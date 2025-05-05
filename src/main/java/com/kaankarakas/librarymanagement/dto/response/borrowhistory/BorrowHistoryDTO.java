package com.kaankarakas.librarymanagement.dto.response.borrowhistory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Borrow history record with user and book details")
public class BorrowHistoryDTO {
    @Schema(description = "Unique ID of the borrow history record", example = "101")
    private Long id;

    @Schema(description = "ID of the user who borrowed the book", example = "12")
    private Long userId;

    @Schema(description = "ID of the borrowed book", example = "45")
    private Long bookId;

    @Schema(description = "Name of the user", example = "Jane Doe")
    private String userName;

    @Schema(description = "Title of the borrowed book", example = "Clean Code")
    private String bookTitle;

    @Schema(description = "Date when the book was borrowed", example = "2025-05-01")
    private LocalDate borrowDate;

    @Schema(description = "Due date for returning the book", example = "2025-05-15")
    private LocalDate dueDate;

    @Schema(description = "Date when the book was returned, if returned", example = "2025-05-10")
    private LocalDate returnDate;

    @Schema(description = "Whether the book has been returned or not", example = "false")
    private Boolean isReturned;
}
