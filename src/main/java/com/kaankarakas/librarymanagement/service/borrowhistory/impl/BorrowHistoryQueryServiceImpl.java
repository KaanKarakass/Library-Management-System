package com.kaankarakas.librarymanagement.service.borrowhistory.impl;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.dto.response.borrowhistory.BorrowHistoryDTO;
import com.kaankarakas.librarymanagement.enums.UserStatus;
import com.kaankarakas.librarymanagement.mapper.borrowhistory.BorrowHistoryMapper;
import com.kaankarakas.librarymanagement.repository.barrowhistory.BorrowHistoryRepository;
import com.kaankarakas.librarymanagement.repository.user.UserRepository;
import com.kaankarakas.librarymanagement.service.borrowhistory.BorrowHistoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kaankarakas.librarymanagement.validator.borrowhistory.BorrowHistoryServiceValidationRule.ERR_USER_NOT_ELIGIBLE;
import static com.kaankarakas.librarymanagement.validator.user.UserServiceValidationRule.ERR_USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BorrowHistoryQueryServiceImpl implements BorrowHistoryQueryService {
    private final BorrowHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final BorrowHistoryMapper borrowHistoryMapper;

    private User checkUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LibraryException(ERR_USER_NOT_FOUND.getDescription(), HttpStatus.NOT_FOUND));
        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new LibraryException(ERR_USER_NOT_ELIGIBLE.getDescription(), HttpStatus.BAD_REQUEST);
        }
        return user;
    }

    @Override
    public List<BorrowHistoryDTO> getUserHistory(Long userId) {
        User user = checkUserById(userId);
        return historyRepository.findByUser(user).stream()
                .map(borrowHistoryMapper::toDTO)
                .toList();

    }

    @Override
    public List<BorrowHistoryDTO> getAllHistories() {
        return historyRepository.findAll().stream()
                .map(borrowHistoryMapper::toDTO)
                .toList();
    }

    @Override
    public List<BorrowHistoryDTO> getOverdueReports() {
       //yazılacak.
        return null;
    }
}
