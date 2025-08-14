package com.book.book.presentation;

import static org.assertj.core.api.Assertions.assertThat;

import com.book.common.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookControllerTest extends ControllerTestSupport {

  @DisplayName("도서 상세 정보 조회 api - 200 성공")
  @Test
  void getBookDetailInfo() {

    // 실패 테스트 우선 체크
    assertThat("123").isEqualTo("234");
  }
}