package com.book.book.fixture;

import com.book.book.domain.entity.Book;
import com.book.book.domain.entity.vo.BaseInfo;
import com.book.book.domain.entity.vo.ExtraInfo;
import com.book.book.domain.entity.vo.PublishInfo;
import java.time.LocalDate;

public class BookFixture {

  public static Book create() {
    return Book.builder()
        .isbn("1234567890123")
        .baseInfo(
            BaseInfo.builder()
                .title("Book Title")
                .subTitle("Book Sub Title")
                .author("Book Author")
                .build()
        )
        .extraInfo(
            ExtraInfo.builder()
                .description("Book Description")
                .image("https://link.com/Book-Image")
                .price(10000)
                .build()
        )
        .publishInfo(
            PublishInfo.builder()
                .publisher("Book Publisher")
                .published(LocalDate.of(2000, 1, 1))
                .build()
        )
        .build();
  }
}
