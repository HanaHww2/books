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
import com.book.book.application.dto.info.PopularKeywordListInfo;
import com.book.book.application.dto.info.SearchBookListInfo;
import com.book.book.application.dto.query.GetBookDetailQuery;
import com.book.book.application.dto.query.SearchBookListQuery;
import com.book.book.fixture.BookDetailInfoFixture;
import com.book.book.fixture.PopularKeywordListInfoFixture;
import com.book.book.fixture.SearchBookListInfoFixture;
import com.book.common.support.ControllerTestSupport;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
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

  @DisplayName("도서 정보 검색 api - 200 성공")
  @Test
  void searchBookList() throws Exception {

    // given
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("keyword", "test");
    params.add("page", "1");
    params.add("size", "10");

    SearchBookListInfo bookListInfo = SearchBookListInfoFixture.create();
    when(bookReadService.searchBookListBy(any(SearchBookListQuery.class), any(Pageable.class)))
        .thenReturn(bookListInfo);

    // when
    ResultActions resultActions = mockMvc.perform(
        get("/api/v1/books/list")
            .params(params));

    // then
    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.searchQuery").value(bookListInfo.searchQuery()))
        .andExpect(jsonPath("$.searchMetaData.strategy").value(bookListInfo.searchMetaData().strategy()))
        .andExpect(jsonPath("$.searchMetaData.executionTime").value(bookListInfo.searchMetaData().executionTime()))
        .andExpect(jsonPath("$.pageInfo.currentPage").value(bookListInfo.pageInfo().currentPage()))
        .andExpect(jsonPath("$.pageInfo.pageSize").value(bookListInfo.pageInfo().pageSize()))
        .andExpect(jsonPath("$.pageInfo.totalPages").value(bookListInfo.pageInfo().totalPages()))
        .andExpect(jsonPath("$.pageInfo.totalElements").value(bookListInfo.pageInfo().totalElements()))
        .andExpect(jsonPath("$.books[0].isbn").value(bookListInfo.books().get(0).isbn()));
  }

  @DisplayName("도서 정보 검색 api - 400 키워드 유효성 검증 실패")
  @Test
  void searchBookList_failed_with_invalidKeyword() throws Exception {

    // given
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("keyword", "카네기 인간관계론-딸의 모습");
    params.add("page", "1");
    params.add("size", "10");

    // when
    ResultActions resultActions = mockMvc.perform(
        get("/api/v1/books/list")
            .params(params));

    // then
    resultActions.andExpect(status().isBadRequest())
        .andExpect(rs -> assertThat(rs.getResolvedException())
            .isExactlyInstanceOf(MethodArgumentNotValidException.class))
        .andExpect(jsonPath("$.errors[0].message")
            .value("잘못된 키워드입니다. (키워드는 최대 2개까지 가능하며, '|' 또는 '-' 으로만 연결해야 하며 공백이 있어서는 안 됩니다.)"))
        .andDo(print());
    verifyNoInteractions(bookReadService);
  }

  @DisplayName("인기 검색어 10 조회 - 200 성공")
  @Test
  void getTop10KeywordsInPrevHour() throws Exception {

    // given
    PopularKeywordListInfo keywordListInfo = PopularKeywordListInfoFixture.create();
    when(bookReadService.getTop10KeywordListInfoInPrevHour())
        .thenReturn(keywordListInfo);

    // when
    ResultActions resultActions = mockMvc.perform(
        get("/api/v1/books/popular-keywords"));

    // then
    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.keywords[0].keyword")
            .value(keywordListInfo.keywords().get(0).keyword()))
        .andExpect(jsonPath("$.keywords[0].hits")
            .value(keywordListInfo.keywords().get(0).hits()))
        .andExpect(jsonPath("$.keywords[0].rank")
            .value(keywordListInfo.keywords().get(0).rank()));
  }
}