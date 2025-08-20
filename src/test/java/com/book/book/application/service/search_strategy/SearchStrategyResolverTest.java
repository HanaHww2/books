package com.book.book.application.service.search_strategy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SearchStrategyResolverTest {

  @DisplayName("키워드에 따라 올바른 전략이 반환되는지 확인")
  @Test
  void resolve_success() {
    // given
    var basic = new SearchBasicStrategy();
    var or = new SearchOrStrategy();
    var not = new SearchNotStrategy();

    var resolver = new SearchStrategyResolver(List.of(basic, or, not));

    // when & then
    assertThat(resolver.resolve("카네기")).isEqualTo(basic);
    assertThat(resolver.resolve("카네기|딸")).isEqualTo(or);
    assertThat(resolver.resolve("카네기-딸")).isEqualTo(not);
  }
}