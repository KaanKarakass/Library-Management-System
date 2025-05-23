package com.kaankarakas.librarymanagement.dto.request.book;

import com.kaankarakas.librarymanagement.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

import static com.kaankarakas.librarymanagement.constants.LibraryManagementDefinitionConstants.*;

@Data
@Builder
public class CreateBookRequest {
    @NotNull
    @Size(max = NAME_MAX_LENGTH)
    @Schema(description = "Title of the book", example = "The Great Gatsby", maxLength = 255)
    private String title;

    @NotNull
    @Size(max = SHORT_NAME_MAX_LENGTH)
    @Schema(description = "Author of the book", example = "F. Scott Fitzgerald", maxLength = 100)
    private String author;

    @NotNull
    @Size(max = CODE_MAX_LENGTH)
    @Schema(description = "ISBN of the book", example = "9784507", maxLength = 20)
    private String isbn;

    @NotNull
    @Schema(description = "Publication date of the book", example = "1925-04-10")
    private LocalDate publicationDate;

    @NotNull
    @Schema(description = "Genre of the book", example = "FICTION")
    private Genre genre;
}
