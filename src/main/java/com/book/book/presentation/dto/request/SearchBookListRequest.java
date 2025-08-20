package com.book.book.presentation.dto.request;

import com.book.book.application.dto.query.SearchBookListQuery;
import jakarta.validation.constraints.Pattern;

public record SearchBookListRequest(
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+([|-][a-zA-Z0-9가-힣]+)?$",
        message = "잘못된 키워드입니다. (키워드는 최대 2개까지 가능하며, '|' 또는 '-' 으로만 연결해야 하며 공백이 있어서는 안 됩니다.)")
    String keyword
) {

  public SearchBookListQuery toQuery() {
    return SearchBookListQuery.builder()
        .keyword(keyword)
        .build();
  }
}
