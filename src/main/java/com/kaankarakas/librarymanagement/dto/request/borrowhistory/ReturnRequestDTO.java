package com.kaankarakas.librarymanagement.dto.request.borrowhistory;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReturnRequestDTO {
    private Long borrowHistoryId;
    private LocalDate returnDate;
}
