package com.book.book.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.book.book.application.dto.info.BookDetailInfo;
import com.book.book.application.dto.info.PopularKeywordListInfo;
import com.book.book.application.dto.info.SearchBookListInfo;
import com.book.book.application.dto.query.GetBookDetailQuery;
import com.book.book.application.dto.query.SearchBookListQuery;
import com.book.book.application.exception.BookErrorCode;
import com.book.book.application.service.search_strategy.SearchStrategy;
import com.book.book.application.service.search_strategy.SearchStrategyResolver;
import com.book.book.application.service.search_strategy.SearchStrategyType;
import com.book.book.domain.entity.Book;
import com.book.book.domain.info.PopularKeyword;
import com.book.book.domain.repository.BookRepository;
import com.book.book.domain.repository.PopularKeywordRepository;
import com.book.book.fixture.BookFixture;
import com.book.common.exception.CommonApiException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookReadServiceTest {

  @Mock
  BookRepository bookRepository;

  @Mock
  PopularKeywordRepository popularKeywordRepository;

  @Mock
  private SearchStrategyResolver searchStrategyResolver;

  @InjectMocks
  BookReadService service;

  @DisplayName("도서 정보 조회 - isbn과 id 둘 다 있는 경우 isbn으로 조회")
  @Test
  void findBookByIsbn_WhenIsbnAndIdPresent() {
    // given
    String isbn = "1234567890123";
    Long id = 999L;

    Book mockBook = mock(Book.class);
    BookDetailInfo mapped = mock(BookDetailInfo.class);

    when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(mockBook));

    try (MockedStatic<BookDetailInfo> mocked = mockStatic(BookDetailInfo.class)) {
      mocked.when(() -> BookDetailInfo.from(mockBook)).thenReturn(mapped);

      // when
      BookDetailInfo result = service.findBookDetailInfoBy(new GetBookDetailQuery(isbn, id));

      // then
      assertThat(result).isSameAs(mapped);
      verify(bookRepository).findByIsbn(isbn);
      verify(bookRepository, never()).findById(anyLong());
    }
  }

  @DisplayName("도서 정보 조회 - id 조건만 있는 경우 id로 조회")
  @Test
  void findBookById_whenIsbnBlankAndIdPresent() {
    // given
    String isbn = "   ";
    Long id = 10L;
    Book mockBook = mock(Book.class);
    BookDetailInfo mapped = mock(BookDetailInfo.class);

    when(bookRepository.findById(id)).thenReturn(Optional.of(mockBook));

    try (MockedStatic<BookDetailInfo> mocked = mockStatic(BookDetailInfo.class)) {
      mocked.when(() -> BookDetailInfo.from(mockBook)).thenReturn(mapped);

      // when
      BookDetailInfo result = service.findBookDetailInfoBy(new GetBookDetailQuery(isbn, id));

      // then
      assertThat(result).isSameAs(mapped);
      verify(bookRepository, never()).findByIsbn(anyString());
      verify(bookRepository).findById(id);
    }
  }

  @DisplayName("도서 정보 조회 - isbn 조건만 있는 경우, 존재하지 않는 isbn 정보로 실패")
  @Test
  void failed_FindBookByIsbn_WhenIsbnPresent_NotFound() {
    // given
    String isbn = "0000000000000";
    when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

    // when/then
    assertThatThrownBy(() ->
        service.findBookDetailInfoBy(new GetBookDetailQuery(isbn, null))
    )
        .isInstanceOf(CommonApiException.class)
        .matches(ex -> ex instanceof CommonApiException be
                && be.getCode().equals(BookErrorCode.NOT_FOUND_ISBN.name()),
            "존재하지 않는 도서 isbn입니다.");

    verify(bookRepository).findByIsbn(isbn);
    verify(bookRepository, never()).findById(anyLong());
  }

  @DisplayName("도서 정보 조회 - id 조건만 있는 경우, 존재하지 않는 id 정보로 실패")
  @Test
  void failed_FindBookById_WhenIdPresent_NotFound() {
    // given
    Long id = 42L;
    when(bookRepository.findById(id)).thenReturn(Optional.empty());

    // when/then
    assertThatThrownBy(() ->
        service.findBookDetailInfoBy(new GetBookDetailQuery(null, id))
    )
        .isInstanceOf(CommonApiException.class)
        .matches(ex -> ex instanceof CommonApiException be
                && be.getCode().equals(BookErrorCode.NOT_FOUND_ID.name()),
            "존재하지 않는 도서 id입니다.");

    verify(bookRepository).findById(id);
    verify(bookRepository, never()).findByIsbn(anyString());
  }

  @DisplayName("도서 정보 검색 - 단순 검색어 활용 성공")
  @Test
  void searchBookListBy_with_BasicOperation_success() {
    // given
    String keyword = "카네기";
    Pageable pageable = PageRequest.of(0, 10);
    SearchBookListQuery query = new SearchBookListQuery(keyword);

    // 전략 mock
    SearchStrategy strategy = mock(SearchStrategy.class);
    given(searchStrategyResolver.resolve(keyword)).willReturn(strategy);
    given(strategy.buildQuery(keyword)).willReturn("'카네기'");
    given(strategy.type()).willReturn(SearchStrategyType.BASIC_OPERATION);

    String[] keywords = {"카네기"};
    given(strategy.getKeywords(keyword)).willReturn(keywords);

    List<Book> books = List.of(
        BookFixture.of("카네기 인간관계론", "9786543210123"),
        BookFixture.of("카네기의 성공 습관", "9786543210124")
    );
    Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());
    given(bookRepository.searchBooks("'카네기'", pageable)).willReturn(bookPage);

    // when
    SearchBookListInfo result = service.searchBookListBy(query, pageable);

    // then
    verify(popularKeywordRepository).record(keywords, SearchStrategyType.BASIC_OPERATION);
    assertThat(result.books()).hasSize(2);
    assertThat(result.searchQuery()).isEqualTo("카네기");
    assertThat(result.searchMetaData().strategy()).isEqualTo("BASIC_OPERATION");
    assertThat(result.searchMetaData().executionTime()).isNotNegative();
  }

  @DisplayName("지난 1시간 Top10 키워드 조회 - 성공")
  @Test
  void getTop10KeywordListInfoInPrevHour_success() {
    // given
    List<PopularKeyword> mockKeywords = List.of(
        PopularKeyword.of("java", 100L, 1),
        PopularKeyword.of("spring", 90L, 2),
        PopularKeyword.of("docker", 80L, 3)
    );
    given(popularKeywordRepository.top10KeywordsInPrevHour())
        .willReturn(mockKeywords);

    // when
    PopularKeywordListInfo result = service.getTop10KeywordListInfoInPrevHour();

    // then
    assertThat(result).isNotNull();
    assertThat(result.keywords()).hasSize(3);
    assertThat(result.keywords().get(0).keyword()).isEqualTo("java");
    assertThat(result.keywords().get(0).hits()).isEqualTo(100L);
    assertThat(result.keywords().get(0).rank()).isEqualTo(1);

    // repository 메서드가 실제 호출됐는지 검증
    verify(popularKeywordRepository).top10KeywordsInPrevHour();
  }
}