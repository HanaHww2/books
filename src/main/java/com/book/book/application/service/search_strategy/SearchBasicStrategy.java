package com.book.book.application.service.search_strategy;

import org.springframework.stereotype.Component;

@Component
public class SearchBasicStrategy implements SearchStrategy {

  @Override
  public SearchStrategyType type() {
    return SearchStrategyType.BASIC_OPERATION;
  }

  @Override
  public String buildQuery(String keyword) {
    return "'" + keyword + "'";
  }

  @Override
  public String[] getKeywords(String keyword) {
    return new String[] {keyword};
  }
}
