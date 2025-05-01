package com.kaankarakas.librarymanagement.repository.book;

import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    boolean existsByIsbn(String isbn);
}
