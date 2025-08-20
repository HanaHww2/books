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

  @DisplayName("구분자가 없으면 원본 문자열 하나만 반환")
  @Test
  void getKeywords_returnSingleKeyword() {
    // given
    String keyword = "카네기";

    // when
    String[] result = strategy.getKeywords(keyword);

    // then
    assertThat(result).containsExactly("카네기");
  }
}