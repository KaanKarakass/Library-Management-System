package com.kaankarakas.librarymanagement.repository.barrowhistory;

import com.kaankarakas.librarymanagement.domain.borrowhistory.BorrowHistory;
import com.kaankarakas.librarymanagement.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowHistoryRepository extends JpaRepository<BorrowHistory, Long> {
    List<BorrowHistory> findByUser(User user);

    List<BorrowHistory> findByIsReturnedFalseAndDueDateBefore(LocalDate date);
}
