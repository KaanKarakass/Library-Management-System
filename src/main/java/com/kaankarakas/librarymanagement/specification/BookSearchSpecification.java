package com.kaankarakas.librarymanagement.specification;

import com.kaankarakas.librarymanagement.domain.book.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSearchSpecification {

    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String ISBN = "ISBN";
    private static final String GENRE = "genre";

    public static Specification<Book> specificationTitle(String title) {
        return (root, query, criteriaBuilder) ->
                title != null ? criteriaBuilder.like(root.get(TITLE), "%" + title + "%") : null;
    }

    public static Specification<Book> specificationAuthor(String author) {
        return (root, query, criteriaBuilder) ->
                author != null ? criteriaBuilder.like(root.get(AUTHOR), "%" + author + "%") : null;
    }

    public static Specification<Book> specificationIsbn(String isbn) {
        return (root, query, criteriaBuilder) ->
                isbn != null ? criteriaBuilder.equal(root.get(ISBN), isbn) : null;
    }

    public static Specification<Book> specificationGenre(String genre) {
        return (root, query, criteriaBuilder) ->
                genre != null ? criteriaBuilder.equal(root.get(GENRE), genre) : null;
    }
}
