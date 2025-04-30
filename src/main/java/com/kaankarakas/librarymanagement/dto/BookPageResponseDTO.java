package com.kaankarakas.librarymanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Paginated response for books")
public class BookPageResponseDTO {
    @Schema(description = "List of books for current page")
    private List<BookDTO> books;

    @Schema(description = "Total number of books")
    private long totalElements;

    @Schema(description = "Total number of pages")
    private int totalPages;

    @Schema(description = "Current page number (zero-based)")
    private int page;

    @Schema(description = "Number of elements per page")
    private int size;
}
