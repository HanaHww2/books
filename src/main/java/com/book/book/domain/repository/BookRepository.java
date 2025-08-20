package com.book.book.domain.repository;

import com.book.book.domain.entity.Book;
import com.book.book.domain.info.BookSimpleInfo;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepository {

  Optional<Book> findByIsbn(String isbn);

  Optional<Book> findById(Long id);

  Book save(Book book);

  Page<BookSimpleInfo> searchBooks(String tsquery, Pageable pageable);
}
