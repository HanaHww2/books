package com.book.book.infrastructure.persistence.jpa;

import com.book.book.domain.entity.Book;
import com.book.book.domain.info.BookSimpleInfo;
import com.book.book.domain.repository.BookRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookJpaRepository
    extends JpaRepository<Book, Long>, BookJpaRepositoryCustom, BookRepository {

  Optional<Book> findByIsbn(String isbn);

  @Query(
      value = """
      SELECT 
              b.id, 
              b.title, 
              b.sub_title, 
              b.author, 
              b.image, 
              b.isbn, 
              b.published
      FROM books b
      WHERE b.tsv @@ to_tsquery(:tsquery)
      ORDER BY b.published DESC
    """,
        countQuery = """
      SELECT count(*)
      FROM books b
      WHERE b.tsv @@ to_tsquery(:tsquery)
    """,
    nativeQuery = true
  )
  Page<BookSimpleInfo> searchBooks(
      @Param("tsquery") String tsquery,
      Pageable pageable
  );
}
