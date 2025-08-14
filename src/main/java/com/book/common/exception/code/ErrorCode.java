package com.book.common.exception.code;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

  String name();
  String getMessage();
  HttpStatus getStatus();
}
