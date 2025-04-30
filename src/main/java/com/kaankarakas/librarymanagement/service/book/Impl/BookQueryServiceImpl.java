package com.kaankarakas.librarymanagement.service.book.Impl;

import com.kaankarakas.librarymanagement.api.request.book.SearchBookRequest;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.dto.BookPageResponseDTO;
import com.kaankarakas.librarymanagement.mapper.book.BookMapper;
import com.kaankarakas.librarymanagement.repository.book.BookRepository;
import com.kaankarakas.librarymanagement.service.book.BookQueryService;
import com.kaankarakas.librarymanagement.specification.BookSearchSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.kaankarakas.librarymanagement.validator.book.BookServiceValidationRule.ERR_BOOK_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BookQueryServiceImpl implements BookQueryService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public Book findBookById(UUID id) {
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ERR_BOOK_NOT_FOUND.getDescription()));
    }

    @Override
    public BookPageResponseDTO searchBooks(SearchBookRequest searchBookRequest) {
        Specification<Book> bookSpecification =
                Specification.where(BookSearchSpecification.specificationTitle(searchBookRequest.getTitle()))
                .and(BookSearchSpecification.specificationAuthor(searchBookRequest.getAuthor()))
                .and(BookSearchSpecification.specificationGenre(String.valueOf(searchBookRequest.getGenre())))
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
