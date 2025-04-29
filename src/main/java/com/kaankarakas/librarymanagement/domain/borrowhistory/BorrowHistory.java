package com.kaankarakas.librarymanagement.domain.borrowhistory;

import com.kaankarakas.librarymanagement.api.constants.SchemaConstants;
import com.kaankarakas.librarymanagement.domain.base.BaseEntity;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(schema = SchemaConstants.LIBRARY_MANAGEMENT_SCHEMA,
        indexes = {
                @Index(columnList = "borrowDate"),
                @Index(columnList = "dueDate"),
                @Index(columnList = "returnDate")
        })
public class BorrowHistory extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NotNull
    private LocalDate borrowDate;

    @NotNull
    private LocalDate dueDate;

    private LocalDate returnDate;

    @NotNull
    private Boolean isReturned = false;
}
