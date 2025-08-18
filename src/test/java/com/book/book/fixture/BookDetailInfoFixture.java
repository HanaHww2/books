package com.book.book.fixture;

import com.book.book.application.dto.info.BookDetailInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookDetailInfoFixture {

  public static BookDetailInfo create() {
    return BookDetailInfo.builder()
        .id(1L)
        .isbn("123456789")
        .title("Book Title")
        .subTitle("Book Sub Title")
        .author("Book Author")
        .description("Book Description")
        .image("https://link.com/Book-Image")
        .price(10000)
        .publisher("Book Publisher")
        .published(LocalDate.of(2000, 1, 1))
        .createdAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
        .updatedAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
        .deletedAt(null)
        .build();
  }
}
