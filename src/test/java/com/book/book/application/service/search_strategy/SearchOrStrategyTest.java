package com.book.book.application.service.search_strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SearchOrStrategyTest {
  private final SearchOrStrategy strategy = new SearchOrStrategy();

  @DisplayName("SearchStrategyType이 OR_OPERATION인지 확인")
  @Test
  void type() {
    assertThat(strategy.type()).isEqualTo(SearchStrategyType.OR_OPERATION);
  }

  @DisplayName("OR 연산 키워드를 tsquery로 변환")
  @Test
  void buildQuery() {
    // given
    String keyword = "카네기|딸";

    // when
    String tsquery = strategy.buildQuery(keyword);

    // then
    assertThat(tsquery).isEqualTo("'카네기' | '딸'");
  }

  @DisplayName("'|' 구분자로 키워드를 분리하고 모든 요소 반환")
  @Test
  void getKeywords_returnAllSplitedByPipe() {
    // given
    String keyword = "카네기|딸";

    // when
    String[] result = strategy.getKeywords(keyword);

    // then
    assertThat(result).containsExactly("카네기", "딸");
  }
}