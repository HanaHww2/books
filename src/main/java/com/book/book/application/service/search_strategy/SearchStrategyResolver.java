package com.book.book.application.service.search_strategy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SearchStrategyResolver {

  private final Map<SearchStrategyType, SearchStrategy> strategyMap;

  public SearchStrategyResolver(List<SearchStrategy> strategies) {
    this.strategyMap = strategies.stream()
        .collect(Collectors.toMap(
            SearchStrategy::type,
            Function.identity())
        );
  }

  public SearchStrategy resolve(String keyword) {
    SearchStrategyType type = SearchStrategyType.parse(keyword);
    return strategyMap.get(type);
  }
}
