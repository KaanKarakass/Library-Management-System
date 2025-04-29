package com.kaankarakas.librarymanagement.repository.barrowhistory;

import com.kaankarakas.librarymanagement.domain.borrowhistory.BorrowHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowHistoryRepository extends JpaRepository<BorrowHistory, Long> {

}
