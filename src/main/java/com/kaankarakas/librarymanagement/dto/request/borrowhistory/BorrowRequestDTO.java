package com.kaankarakas.librarymanagement.dto.request.borrowhistory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowRequestDTO {
    @Schema(description = "ID of the book to borrow", example = "5")
    private Long bookId;

    @Schema(description = "Date the book was borrowed", example = "2025-05-05")
    private LocalDate borrowDate;

    @Schema(description = "Due date to return the book", example = "2025-05-15")
    private LocalDate dueDate;
}
