package com.book.book.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.book.book.application.dto.info.BookDetailInfo;
import com.book.book.application.dto.query.GetBookDetailQuery;
import com.book.book.application.exception.BookErrorCode;
import com.book.book.domain.entity.Book;
import com.book.book.domain.repository.BookRepository;
import com.book.common.exception.CommonApiException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookReadServiceTest {

  @Mock
  BookRepository bookRepository;

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
}