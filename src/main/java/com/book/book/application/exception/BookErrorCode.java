package com.book.book.application.exception;

import com.book.common.exception.code.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BookErrorCode implements ErrorCode {
  NOT_FOUND_ID("존재하지 않는 도서 아이디입니다.", HttpStatus.NOT_FOUND),
  NOT_FOUND_ISBN("존재하지 않는 도서 isbn입니다.", HttpStatus.NOT_FOUND),
  ;

  private final String message;
  private final HttpStatus status;

}
