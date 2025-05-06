package com.kaankarakas.librarymanagement.service.borrowhistory;

import com.kaankarakas.librarymanagement.dto.request.borrowhistory.BorrowRequestDTO;
import com.kaankarakas.librarymanagement.dto.request.borrowhistory.ReturnRequestDTO;
import com.kaankarakas.librarymanagement.dto.response.borrowhistory.BorrowHistoryDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface BorrowHistoryCommandService {
    BorrowHistoryDTO borrowBook(@NotNull  BorrowRequestDTO dto);

    BorrowHistoryDTO returnBook(@NotNull Long historyId, @NotNull ReturnRequestDTO dto);


}
