package com.book.data_set.naver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NaverBookResponse(List<NaverBookItem> items) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record NaverBookItem(
      String title,
      String link,
      String image,
      String author,
      String discount,
      String publisher,
      String pubdate,
      String isbn,
      String description
  ) {}
}