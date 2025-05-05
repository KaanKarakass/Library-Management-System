package com.kaankarakas.librarymanagement.service.borrowhistory.impl;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.domain.borrowhistory.BorrowHistory;
import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.dto.request.borrowhistory.BorrowRequestDTO;
import com.kaankarakas.librarymanagement.dto.request.borrowhistory.ReturnRequestDTO;
import com.kaankarakas.librarymanagement.dto.response.borrowhistory.BorrowHistoryDTO;
import com.kaankarakas.librarymanagement.enums.BookStatus;
import com.kaankarakas.librarymanagement.enums.UserStatus;
import com.kaankarakas.librarymanagement.mapper.borrowhistory.BorrowHistoryMapper;
import com.kaankarakas.librarymanagement.repository.barrowhistory.BorrowHistoryRepository;
import com.kaankarakas.librarymanagement.repository.book.BookRepository;
import com.kaankarakas.librarymanagement.repository.user.UserRepository;
import com.kaankarakas.librarymanagement.security.SecurityUtil;
import com.kaankarakas.librarymanagement.service.borrowhistory.BorrowHistoryCommandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.time.LocalDate;

import static com.kaankarakas.librarymanagement.validator.borrowhistory.BorrowHistoryServiceValidationRule.*;
import static com.kaankarakas.librarymanagement.validator.user.UserServiceValidationRule.ERR_USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BorrowHistoryCommandServiceImpl implements BorrowHistoryCommandService {

    private final BorrowHistoryRepository historyRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowHistoryMapper borrowHistoryMapper;

    private User checkUserByUsername(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new LibraryException(ERR_USER_NOT_FOUND.getDescription(), HttpStatus.NOT_FOUND));
        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new LibraryException(ERR_USER_NOT_ELIGIBLE.getDescription(), HttpStatus.BAD_REQUEST);
        }
        return user;
    }

    private Book checkBookById(Long bookId) {
        return bookRepository.findByIdAndBookStatus(bookId, BookStatus.ACTIVE)
                .orElseThrow(() -> new LibraryException(ERR_USER_NOT_AVAILABLE.getDescription(), HttpStatus.BAD_REQUEST));
    }

    private BorrowHistory prepareBorrowHistory(User user, Book book, BorrowRequestDTO request) {
        BorrowHistory borrowHistory = new BorrowHistory();
        borrowHistory.setBook(book);
        borrowHistory.setUser(user);
        borrowHistory.setBorrowDate(request.getBorrowDate());
        borrowHistory.setDueDate(request.getDueDate());
        borrowHistory.setIsReturned(false);
        return borrowHistory;
    }

    private String getUsernameFromUtil() {
        return SecurityUtil.getCurrentUsername();
    }

    @Transactional
    @Override
    public BorrowHistoryDTO borrowBook(BorrowRequestDTO request) {
        User user = checkUserByUsername(getUsernameFromUtil());
        Book book = checkBookById(request.getBookId());

        book.setBookStatus(BookStatus.BORROWED);
        bookRepository.save(book);

        BorrowHistory borrowHistory = historyRepository.save(prepareBorrowHistory(user, book, request));
        return borrowHistoryMapper.toDTO(borrowHistory);
    }

    private BorrowHistory checkBorrowHistoryById(Long historyId) {
        BorrowHistory borrowHistory = historyRepository.findById(historyId)
                .orElseThrow(() -> new LibraryException(ERR_BORROW_RECORD_NOT_FOUND.getDescription(), HttpStatus.NOT_FOUND));
        if (borrowHistory.getIsReturned()) {
            throw new LibraryException(ERR_BOOK_ALREADY_RETURNED.getDescription(), HttpStatus.NOT_FOUND);
        }
        return borrowHistory;
    }

    @Transactional
    @Override
    public BorrowHistoryDTO returnBook(Long historyId, ReturnRequestDTO request) {
        User user = checkUserByUsername(getUsernameFromUtil());
        BorrowHistory borrowHistory = checkBorrowHistoryById(historyId);

        if (!user.getId().equals(borrowHistory.getUser().getId())) {
            throw new LibraryException(ERR_INVALID_USER.getDescription(), HttpStatus.FORBIDDEN);
        }

        borrowHistory.setReturnDate(request != null && request.getReturnDate() != null
                ? request.getReturnDate()
                : LocalDate.now());

        borrowHistory.setIsReturned(true);

        Book book = borrowHistory.getBook();
        book.setBookStatus(BookStatus.ACTIVE);
        bookRepository.save(book);

        return borrowHistoryMapper.toDTO(historyRepository.save(borrowHistory));
    }

}
