package com.book.book.application.service.search_strategy;

import org.springframework.stereotype.Component;

@Component
public class SearchOrStrategy implements SearchStrategy {

  @Override
  public SearchStrategyType type() {
    return SearchStrategyType.OR_OPERATION;
  }

  @Override
  public String buildQuery(String keyword) {
    String[] keywords = keyword.split("\\|");
    return "'" + keywords[0] + "' | '" + keywords[1] + "'";
  }
}
