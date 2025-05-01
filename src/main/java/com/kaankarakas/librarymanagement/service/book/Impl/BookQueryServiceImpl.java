package com.kaankarakas.librarymanagement.service.book.Impl;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.dto.request.book.SearchBookRequest;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.dto.response.book.BookPageResponseDTO;
import com.kaankarakas.librarymanagement.enums.Genre;
import com.kaankarakas.librarymanagement.enums.BookStatus;
import com.kaankarakas.librarymanagement.mapper.book.BookMapper;
import com.kaankarakas.librarymanagement.repository.book.BookRepository;
import com.kaankarakas.librarymanagement.service.book.BookQueryService;
import com.kaankarakas.librarymanagement.specification.BookSearchSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import static com.kaankarakas.librarymanagement.validator.book.BookServiceValidationRule.ERR_BOOK_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BookQueryServiceImpl implements BookQueryService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public Book findBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new LibraryException(ERR_BOOK_NOT_FOUND.getDescription(), HttpStatus.NOT_FOUND));
    }

    @Override
    public BookPageResponseDTO searchBooks(SearchBookRequest searchBookRequest) {
        Specification<Book> bookSpecification =
                Specification.where(BookSearchSpecification.specificationTitle(searchBookRequest.getTitle()))
                        .and(BookSearchSpecification.specificationAuthor(searchBookRequest.getAuthor()))
                        .and(BookSearchSpecification.specificationGenre(searchBookRequest.getGenre() != null ? Genre.valueOf(searchBookRequest.getGenre()) : null))
                        .and(BookSearchSpecification.specificationIsbn(searchBookRequest.getIsbn()));

        Pageable pageable = PageRequest.of(searchBookRequest.getPage(), searchBookRequest.getSize());

        Page<Book> bookPage = bookRepository.findAll(bookSpecification, pageable);
        return BookPageResponseDTO.builder()
                .books(bookPage.getContent().stream().map(bookMapper::toDTO).toList())
                .totalElements(bookPage.getTotalElements())
                .totalPages(bookPage.getTotalPages())
                .page(bookPage.getNumber())
                .size(bookPage.getSize())
                .build();
    }

}
