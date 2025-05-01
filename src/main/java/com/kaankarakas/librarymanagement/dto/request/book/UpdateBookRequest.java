package com.kaankarakas.librarymanagement.dto.request.book;

import com.kaankarakas.librarymanagement.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

import static com.kaankarakas.librarymanagement.constants.LibraryManagementDefinitionConstants.*;

@Data
@Schema(description = "Request object for updating a book")
public class UpdateBookRequest {

    @Size(max = NAME_MAX_LENGTH)
    @Schema(description = "Book title", example = "The Great Gatsby")
    private String title;

    @Size(max = SHORT_NAME_MAX_LENGTH)
    @Schema(description = "Author name", example = "F. Scott Fitzgerald")
    private String author;

    @Size(max = CODE_MAX_LENGTH)
    @Schema(description = "ISBN code", example = "9780141182636")
    private String isbn;

    @Schema(description = "Publication date", example = "1925-04-10")
    private LocalDate publicationDate;

    @Schema(description = "Genre of the book", example = "FICTION")
    private Genre genre;
}
