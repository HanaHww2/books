package com.book.book.domain.info;

import java.time.LocalDate;

public interface BookSimpleInfo {
  Long getId();
  String getTitle();
  String getSubTitle();
  String getAuthor();
  String getImage();
  String getIsbn();
  LocalDate getPublished();
}
