package com.kaankarakas.librarymanagement.dto.request.borrowhistory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReturnRequestDTO {
    @Schema(description = "The ID of the borrow history", example = "123", required = true)
    private Long borrowHistoryId;

    @Schema(description = "The date when the book is returned", example = "2025-05-05", required = true)
    private LocalDate returnDate;
}
