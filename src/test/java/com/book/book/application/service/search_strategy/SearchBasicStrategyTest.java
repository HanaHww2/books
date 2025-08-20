package com.book.book.application.service.search_strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SearchBasicStrategyTest {
  private final SearchBasicStrategy strategy = new SearchBasicStrategy();

  @DisplayName("SearchStrategyType이 BASIC_OPERATION인지 확인")
  @Test
  void type() {
    assertThat(strategy.type()).isEqualTo(SearchStrategyType.BASIC_OPERATION);
  }

  @DisplayName("BASIC 연산 키워드를 tsquery로 변환")
  @Test
  void buildQuery() {
    // given
    String keyword = "카네기";

    // when
    String tsquery = strategy.buildQuery(keyword);

    // then
    assertThat(tsquery).isEqualTo("'카네기'");
  }
}