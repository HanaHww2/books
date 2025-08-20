package com.book.book.application.service.search_strategy;

import java.util.Arrays;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SearchStrategyType {
  OR_OPERATION((val) -> val.contains("|")),
  NOT_OPERATION((val) -> val.contains("-")),
  BASIC_OPERATION((val) -> !val.contains("|") && !val.contains("-")),
  ;
  private final Predicate<String> predicate;

  public static SearchStrategyType parse(String keyword) {
    return Arrays.stream(SearchStrategyType.values())
        .filter(type -> type.predicate.test(keyword))
        .findFirst()
        .orElse(BASIC_OPERATION);
  }
}