package com.kaankarakas.librarymanagement.dto.request.borrowhistory;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowRequestDTO {
    private Long bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
}
