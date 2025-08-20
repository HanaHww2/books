package com.book.book.application.dto.info;

import com.book.book.domain.info.PopularKeyword;
import java.util.List;
import lombok.Builder;

@Builder
public record PopularKeywordListInfo(
    List<PopularKeywordInfo> keywords
) {

  public static PopularKeywordListInfo from(List<PopularKeyword> popularKeywords) {
    return PopularKeywordListInfo.builder()
        .keywords(popularKeywords.stream().map(PopularKeywordInfo::from).toList())
        .build();
  }

  @Builder
  public record PopularKeywordInfo(
      String keyword,
      double hits,
      Integer rank
) {
    public static PopularKeywordInfo from(PopularKeyword popularKeyword) {
      return PopularKeywordInfo.builder()
          .keyword(popularKeyword.keyword())
          .hits(popularKeyword.hits())
          .rank(popularKeyword.rank())
          .build();
    }

    public static PopularKeywordInfo of(String keyword, double hits, Integer rank) {
      return PopularKeywordInfo.builder()
          .keyword(keyword)
          .hits(hits)
          .rank(rank)
          .build();
    }
  }
}
