package com.book.book.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.book.book.application.dto.info.BookDetailInfo;
import com.book.book.application.dto.query.GetBookDetailQuery;
import com.book.book.fixture.BookDetailInfoFixture;
import com.book.common.support.ControllerTestSupport;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.MethodArgumentNotValidException;

class BookControllerTest extends ControllerTestSupport {

  @DisplayName("도서 상세 정보 조회 api - 200 성공")
  @Test
  void getBookDetailInfo() throws Exception {

    // given
    DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("isbn", "1234567890123");

    BookDetailInfo bookDetailInfo = BookDetailInfoFixture.create();
    when(bookReadService.findBookDetailInfoBy(any(GetBookDetailQuery.class)))
        .thenReturn(bookDetailInfo);

    // when
    ResultActions resultActions = mockMvc.perform(
        get("/api/v1/books")
            .params(params));

    // then
    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(bookDetailInfo.id()))
        .andExpect(jsonPath("$.isbn").value(bookDetailInfo.isbn()))
        .andExpect(jsonPath("$.title").value(bookDetailInfo.title()))
        .andExpect(jsonPath("$.subTitle").value(bookDetailInfo.subTitle()))
        .andExpect(jsonPath("$.author").value(bookDetailInfo.author()))
        .andExpect(jsonPath("$.description").value(bookDetailInfo.description()))
        .andExpect(jsonPath("$.price").value(bookDetailInfo.price()))
        .andExpect(jsonPath("$.image").value(bookDetailInfo.image()))
        .andExpect(jsonPath("$.publisher").value(bookDetailInfo.publisher()))
        .andExpect(jsonPath("$.published").value(bookDetailInfo.published().toString()))
        .andExpect(jsonPath("$.createdAt").value(ISO.format(bookDetailInfo.createdAt())))
        .andExpect(jsonPath("$.updatedAt").value(ISO.format(bookDetailInfo.updatedAt())))
        .andExpect(jsonPath("$.deletedAt").value(nullValue()));
  }

  @DisplayName("도서 상세 정보 조회 api - 파라미터 누락으로 인한 400 Bad Request")
  @Test
  void getBookDetailInfo_withoutParams_returns400() throws Exception {

    // when
    ResultActions result = mockMvc.perform(get("/api/v1/books"));

    // then
    result.andExpect(status().isBadRequest())
        .andExpect(rs -> assertThat(rs.getResolvedException())
            .isExactlyInstanceOf(MethodArgumentNotValidException.class))
        .andExpect(jsonPath("$.errors[0].message").value("isbn 혹은 id 조건이 필수입니다."))
        .andDo(print());
    verifyNoInteractions(bookReadService);
  }

  @DisplayName("도서 상세 정보 조회 api - 13자리 초과 isbn으로 인한 400 Bad Request")
  @Test
  void getBookDetailInfo_withInvalidIsbn_returns400() throws Exception {

    // given
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("isbn", "12345678901234");

    // when
    ResultActions result = mockMvc.perform(get("/api/v1/books")
        .params(params));

    // then
    result.andExpect(status().isBadRequest())
        .andExpect(rs -> assertThat(rs.getResolvedException())
            .isExactlyInstanceOf(MethodArgumentNotValidException.class))
        .andExpect(jsonPath("$.errors[0].message").value("isbn의 길이는 13자리입니다."))
        .andDo(print());
    verifyNoInteractions(bookReadService);
  }


  @DisplayName("도서 상세 정보 조회 api - 0 이하 id로 인한 400 Bad Request")
  @Test
  void getBookDetailInfo_withInvalidId_returns400() throws Exception {

    // given
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("id", "-123");

    // when
    ResultActions result = mockMvc.perform(get("/api/v1/books")
        .params(params));

    // then
    result.andExpect(status().isBadRequest())
        .andExpect(rs -> assertThat(rs.getResolvedException())
            .isExactlyInstanceOf(MethodArgumentNotValidException.class))
        .andExpect(jsonPath("$.errors[0].message").value("id는 0보다 큰 수입니다."))
        .andDo(print());
    verifyNoInteractions(bookReadService);
  }
}