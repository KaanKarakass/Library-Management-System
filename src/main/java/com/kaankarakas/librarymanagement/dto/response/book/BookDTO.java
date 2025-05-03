package com.kaankarakas.librarymanagement.dto.response.book;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BookDTO {
    @Schema(description = "Title of the book", example = "The Great Gatsby", maxLength = 255)
    private String title;

    @Schema(description = "Author of the book", example = "F. Scott Fitzgerald", maxLength = 100)
    private String author;

    @Schema(description = "ISBN of the book", example = "9780743273565", maxLength = 20)
    private String isbn;

    @Schema(description = "Publication date of the book", example = "1925-04-10")
    private String publicationDate;

    @Schema(description = "Genre of the book", example = "FICTION")
    private String genre;

    @Schema(description = "Status of the book", example = "ACTIVE")
    private String bookStatus;
}
