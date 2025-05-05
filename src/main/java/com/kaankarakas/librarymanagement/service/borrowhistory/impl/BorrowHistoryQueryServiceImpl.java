package com.kaankarakas.librarymanagement.service.borrowhistory.impl;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.dto.response.borrowhistory.BorrowHistoryDTO;
import com.kaankarakas.librarymanagement.enums.UserStatus;
import com.kaankarakas.librarymanagement.mapper.borrowhistory.BorrowHistoryMapper;
import com.kaankarakas.librarymanagement.repository.barrowhistory.BorrowHistoryRepository;
import com.kaankarakas.librarymanagement.repository.user.UserRepository;
import com.kaankarakas.librarymanagement.security.SecurityUtil;
import com.kaankarakas.librarymanagement.service.borrowhistory.BorrowHistoryQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.kaankarakas.librarymanagement.validator.borrowhistory.BorrowHistoryServiceValidationRule.ERR_USER_NOT_ELIGIBLE;
import static com.kaankarakas.librarymanagement.validator.user.UserServiceValidationRule.ERR_USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowHistoryQueryServiceImpl implements BorrowHistoryQueryService {
    private static final String REPORT_HEADER = "OVERDUE BOOKS REPORT";
    private static final String REPORT_SEPARATOR = "--------------------";
    private static final String TOTAL_OVERDUE_FORMAT = "Total Overdue Records: %d\n";
    private static final String USERS_OVERDUE_FORMAT = "Users with Overdues: %d\n\n";
    private static final String DETAILS_HEADER = "Details:\n";
    private static final String DETAIL_LINE_FORMAT = "- User %d (%s) did not return Book %d (\"%s\") borrowed on %s (due %s)\n";
    private static final String REPORT_FOOTER_PREFIX = "\nGenerated At: ";
    private final BorrowHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final BorrowHistoryMapper borrowHistoryMapper;

    private User checkUserByUsername(String username) {
        log.debug("Checking user with username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new LibraryException(ERR_USER_NOT_FOUND.getDescription(), HttpStatus.NOT_FOUND));
        if (user.getUserStatus() != UserStatus.ACTIVE) {
            log.error("User '{}' is not eligible (status: {})", username, user.getUserStatus());
            throw new LibraryException(ERR_USER_NOT_ELIGIBLE.getDescription(), HttpStatus.BAD_REQUEST);
        }
        return user;
    }

    private String getUsernameFromUtil() {
        return SecurityUtil.getCurrentUsername();
    }

    @Override
    public List<BorrowHistoryDTO> getUserHistory() {
        log.debug("Fetching borrow history for current user");
        User user = checkUserByUsername(getUsernameFromUtil());
        return historyRepository.findByUser(user).stream()
                .map(borrowHistoryMapper::toDTO)
                .toList();

    }

    @Override
    public List<BorrowHistoryDTO> getAllHistories() {
        log.debug("Fetching all borrow histories");
        return historyRepository.findAll().stream()
                .map(borrowHistoryMapper::toDTO)
                .toList();
    }

    @Override
    public String generateOverdueReport() {
        log.debug("Generating overdue report");
        List<BorrowHistoryDTO> overdue = historyRepository
                .findByIsReturnedFalseAndDueDateBefore(LocalDate.now())
                .stream()
                .map(borrowHistoryMapper::toDTO)
                .toList();

        StringBuilder sb = new StringBuilder();
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        sb.append(REPORT_HEADER)
                .append(REPORT_SEPARATOR)
                .append(String.format(TOTAL_OVERDUE_FORMAT, overdue.size()))
                .append(String.format(USERS_OVERDUE_FORMAT,
                        overdue.stream().map(BorrowHistoryDTO::getUserId).distinct().count()))
                .append(DETAILS_HEADER);

        overdue.forEach(dto -> sb.append(
                String.format(
                        DETAIL_LINE_FORMAT,
                        dto.getUserId(),
                        dto.getUserName(),
                        dto.getBookId(),
                        dto.getBookTitle(),
                        dto.getBorrowDate(),
                        dto.getDueDate()
                )
        ));

        sb.append(REPORT_FOOTER_PREFIX)
                .append(LocalDateTime.now().format(fmt));
        log.info("Overdue report generated with {} records", overdue.size());
        return sb.toString();
    }
}
