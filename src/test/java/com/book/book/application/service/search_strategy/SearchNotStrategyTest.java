package com.book.book.application.service.search_strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SearchNotStrategyTest {
  private final SearchNotStrategy strategy = new SearchNotStrategy();

  @DisplayName("SearchStrategyType이 NOT_OPERATION인지 확인")
  @Test
  void type() {
    assertThat(strategy.type()).isEqualTo(SearchStrategyType.NOT_OPERATION);
  }

  @DisplayName("NOT 연산 키워드를 tsquery로 변환")
  @Test
  void buildQuery() {
    // given
    String keyword = "카네기-딸";

    // when
    String tsquery = strategy.buildQuery(keyword);

    // then
    assertThat(tsquery).isEqualTo("'카네기' & !'딸'");
  }

  @Test
  @DisplayName("'-' 구분자로 키워드를 분리하고 첫번째 요소만 반환")
  void getKeywords_returnOnlyFirstSplitedByHyphen() {
    // given
    String keyword = "카네기-딸";

    // when
    String[] result = strategy.getKeywords(keyword);

    // then
    assertThat(result).containsExactly("카네기");
  }
}