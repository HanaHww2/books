package com.book.book.application.service.search_strategy;

public interface SearchStrategy {

  SearchStrategyType type();

  String buildQuery(String keyword);

  String[] getKeywords(String keyword);
}
