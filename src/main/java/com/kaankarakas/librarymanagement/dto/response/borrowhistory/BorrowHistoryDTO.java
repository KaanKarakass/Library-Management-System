package com.kaankarakas.librarymanagement.dto.response.borrowhistory;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowHistoryDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Boolean isReturned;
}
