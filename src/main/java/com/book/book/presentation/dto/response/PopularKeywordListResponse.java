package com.book.book.presentation.dto.response;

import com.book.book.application.dto.info.PopularKeywordListInfo;
import com.book.book.application.dto.info.PopularKeywordListInfo.PopularKeywordInfo;
import java.util.List;
import lombok.Builder;

@Builder
public record PopularKeywordListResponse(
    List<PopularKeywordResponse> keywords
) {

  public static PopularKeywordListResponse from(PopularKeywordListInfo keywordListInfo) {
    return PopularKeywordListResponse.builder()
        .keywords(keywordListInfo.keywords().stream().map(PopularKeywordResponse::from).toList())
        .build();
  }

  @Builder
  record PopularKeywordResponse(
      String keyword,
      double hits,
      Integer rank
  )
  {

    public static PopularKeywordResponse from(PopularKeywordInfo popularKeywordInfo) {
      return PopularKeywordResponse.builder()
          .keyword(popularKeywordInfo.keyword())
          .hits(popularKeywordInfo.hits())
          .rank(popularKeywordInfo.rank())
          .build();
    }
  }
}
