package com.kaankarakas.librarymanagement.service.borrowhistory;

import com.kaankarakas.librarymanagement.dto.response.borrowhistory.BorrowHistoryDTO;

import java.util.List;

public interface BorrowHistoryQueryService {
    List<BorrowHistoryDTO> getUserHistory();

    List<BorrowHistoryDTO> getAllHistories();

    String generateOverdueReport();
}
