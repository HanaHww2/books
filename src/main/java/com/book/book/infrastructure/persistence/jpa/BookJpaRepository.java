package com.book.book.infrastructure.persistence.jpa;

import com.book.book.domain.entity.Book;
import com.book.book.domain.repository.BookRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository
    extends JpaRepository<Book, Long>, BookJpaRepositoryCustom, BookRepository {

  Optional<Book> findByIsbn(String isbn);
}
