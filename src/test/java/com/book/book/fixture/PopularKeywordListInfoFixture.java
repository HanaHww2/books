package com.book.book.fixture;

import com.book.book.application.dto.info.PopularKeywordListInfo;
import com.book.book.application.dto.info.PopularKeywordListInfo.PopularKeywordInfo;
import java.util.List;

public class PopularKeywordListInfoFixture {

  public static PopularKeywordListInfo create() {
    return PopularKeywordListInfo.builder()
        .keywords(List.of(PopularKeywordInfo.of("java", 100L, 1),
            PopularKeywordInfo.of("spring", 90L, 2),
            PopularKeywordInfo.of("docker", 80L, 3)))
        .build();
  }
}
