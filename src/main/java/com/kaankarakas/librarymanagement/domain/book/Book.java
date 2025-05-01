package com.kaankarakas.librarymanagement.domain.book;

import static com.kaankarakas.librarymanagement.constants.LibraryManagementDefinitionConstants.*;

import com.kaankarakas.librarymanagement.api.constants.SchemaConstants;
import com.kaankarakas.librarymanagement.domain.base.BaseEntity;
import com.kaankarakas.librarymanagement.enums.BookStatus;
import com.kaankarakas.librarymanagement.enums.Genre;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(schema = SchemaConstants.LIBRARY_MANAGEMENT_SCHEMA,
        indexes = {
                @Index(columnList = "title"), @Index(columnList = "author"), @Index(columnList = "genre")})
public class Book extends BaseEntity {

    @Size(max = NAME_MAX_LENGTH)
    @NotNull
    private String title;

    @Size(max = SHORT_NAME_MAX_LENGTH)
    @NotNull
    private String author;

    @Size(max = CODE_MAX_LENGTH)
    @NotNull
    private String isbn;

    @NotNull
    private LocalDate publicationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;
}
