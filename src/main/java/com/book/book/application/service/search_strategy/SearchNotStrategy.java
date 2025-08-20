package com.book.book.application.service.search_strategy;

import org.springframework.stereotype.Component;

@Component
public class SearchNotStrategy implements SearchStrategy {

  @Override
  public SearchStrategyType type() {
    return SearchStrategyType.NOT_OPERATION;
  }

  @Override
  public String buildQuery(String keyword) {
    String[] keywords = keyword.split("-");
    return "'" + keywords[0] + "' & !'" + keywords[1] + "'";
  }

  @Override
  public String[] getKeywords(String keyword) {
    return new String[] { keyword.split("-")[0] };
  }
}
