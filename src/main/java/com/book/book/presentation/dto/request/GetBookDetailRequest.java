package com.book.book.presentation.dto.request;

import com.book.book.application.dto.query.GetBookDetailQuery;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public record GetBookDetailRequest(
    @Length(min = 13 ,max = 13, message = "isbn의 길이는 13자리입니다.") String isbn,
    @Positive(message = "id는 0보다 큰 수입니다.") Long id
) {

  @AssertTrue(message = "isbn 혹은 id 조건이 필수입니다.")
  public boolean isEitherIdOrIsbn() {
    return (isbn != null && !isbn.isBlank()) || id != null;
  }

  public GetBookDetailQuery toQuery() {
    return GetBookDetailQuery.builder()
        .isbn(isbn)
        .id(id)
        .build();
  }
}
