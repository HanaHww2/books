package com.book.book.application.service;

import static org.springframework.util.StringUtils.hasText;

import com.book.book.application.dto.info.BookDetailInfo;
import com.book.book.application.dto.info.SearchBookListInfo;
import com.book.book.application.dto.query.GetBookDetailQuery;
import com.book.book.application.dto.query.SearchBookListQuery;
import com.book.book.application.exception.BookErrorCode;
import com.book.book.application.service.search_strategy.SearchStrategy;
import com.book.book.application.service.search_strategy.SearchStrategyResolver;
import com.book.book.domain.entity.Book;
import com.book.book.domain.repository.BookRepository;
import com.book.common.exception.CommonApiException;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookReadService {

  private final SearchStrategyResolver searchStrategyResolver;
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

  public SearchBookListInfo searchBookListBy(SearchBookListQuery query, Pageable pageable) {

    Instant start = Instant.now();
    String keyword = query.keyword();
    SearchStrategy strategy = searchStrategyResolver.resolve(keyword);
    String tsquery = strategy.buildQuery(keyword);

    Page<Book> bookPage = bookRepository.searchBooks(tsquery, pageable);
    Instant end = Instant.now();
    long executionTime = Duration.between(start, end).toMillis();

    return SearchBookListInfo.of(bookPage, keyword, strategy.type().name(), executionTime);
  }
}
