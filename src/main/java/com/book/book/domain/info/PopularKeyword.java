package com.book.book.domain.info;

import lombok.Builder;

@Builder
public record PopularKeyword(
    String keyword,
    double hits,
    Integer rank) {

  public static PopularKeyword of(String keyword, double hits, Integer rank) {
    return PopularKeyword.builder()
        .keyword(keyword)
        .hits(hits)
        .rank(rank)
        .build();
  }
}
