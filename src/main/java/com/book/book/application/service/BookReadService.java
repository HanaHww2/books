package com.book.book.application.service;

import static org.springframework.util.StringUtils.hasText;

import com.book.book.application.dto.info.BookDetailInfo;
import com.book.book.application.dto.query.GetBookDetailQuery;
import com.book.book.application.exception.BookErrorCode;
import com.book.book.domain.entity.Book;
import com.book.book.domain.repository.BookRepository;
import com.book.common.exception.CommonApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookReadService {

  private final BookRepository bookRepository;

  public BookDetailInfo findBookDetailInfoBy(GetBookDetailQuery query) {

    Book book;
    if (hasText(query.isbn())) {
      book = bookRepository.findByIsbn(query.isbn())
          .orElseThrow(() -> CommonApiException.from(BookErrorCode.NOT_FOUND_ISBN));
      return BookDetailInfo.from(book);
    }

    book = bookRepository.findById(query.id())
        .orElseThrow(() -> CommonApiException.from(BookErrorCode.NOT_FOUND_ID));
    return BookDetailInfo.from(book);

  }
}
