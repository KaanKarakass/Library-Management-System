package com.kaankarakas.librarymanagement.dto.request.borrowhistory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowRequestDTO {
    @Schema(description = "ID of the book to borrow", example = "5")
    @NotNull
    private Long bookId;

    @Schema(description = "Date the book was borrowed", example = "2025-05-05")
    @NotNull
    private LocalDate borrowDate;

    @Schema(description = "Due date to return the book", example = "2025-05-15")
    @NotNull
    private LocalDate dueDate;
}
