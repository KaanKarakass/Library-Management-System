package com.kaankarakas.librarymanagement.service.borrowhistory;

import com.kaankarakas.librarymanagement.dto.request.borrowhistory.BorrowRequestDTO;
import com.kaankarakas.librarymanagement.dto.request.borrowhistory.ReturnRequestDTO;
import com.kaankarakas.librarymanagement.dto.response.borrowhistory.BorrowHistoryDTO;

import java.util.List;

public interface BorrowHistoryCommandService {
    BorrowHistoryDTO borrowBook(BorrowRequestDTO dto);

    BorrowHistoryDTO returnBook(Long historyId, ReturnRequestDTO dto);


}
