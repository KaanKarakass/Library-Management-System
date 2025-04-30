package com.kaankarakas.librarymanagement.dto.request.book;

import com.kaankarakas.librarymanagement.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchBookRequest {
    @Schema(description = "Title of the book", example = "Effective Java")
    private String title;

    @Schema(description = "Author of the book", example = "Joshua Bloch")
    private String author;

    @Schema(description = "ISBN number of the book", example = "978-0134685991")
    private String isbn;

    @Schema(description = "Genre of the book", example = "Programming")
    private Genre genre;

    @Schema(description = "Start of the publication date range", example = "2001-01-01")
    private LocalDate fromPublicationDate;

    @Schema(description = "End of the publication date range", example = "2021-12-31")
    private LocalDate toPublicationDate;

    @Schema(description = "Page number for pagination", example = "0")
    private int page = 0;

    @Schema(description = "Page size for pagination", example = "10")
    private int size = 10;
}
