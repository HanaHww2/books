package com.book.book.fixture;

import com.book.book.domain.info.BookSimpleInfo;
import java.time.LocalDate;

public class BookSimpleInfoFixture {

  public static BookSimpleInfo of(String title, String isbn) {
    return new BookSimpleInfo() {
      @Override
      public Long getId() {
        return 1L;
      }

      @Override
      public String getTitle() {
        return title;
      }

      @Override
      public String getSubTitle() {
        return title;
      }

      @Override
      public String getAuthor() {
        return "Author";
      }

      @Override
      public String getImage() {
        return "image-link";
      }

      @Override
      public String getIsbn() {
        return isbn;
      }

      @Override
      public LocalDate getPublished() {
        return LocalDate.of(2000, 1, 1);
      }
    };
  }
}
