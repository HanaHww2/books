package com.book.book.domain.repository;

import com.book.book.domain.entity.Book;
import java.util.Optional;

public interface BookRepository {

  Optional<Book> findByIsbn(String isbn);

  Optional<Book> findById(Long id);

  Book save(Book book);
}
