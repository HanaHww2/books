package com.book.common.exception;

import com.book.common.exception.code.ErrorCode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonApiException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    protected CommonApiException(ErrorCode errorCode) {
      super(errorCode.getMessage());
      this.status = errorCode.getStatus();
      this.code = errorCode.name();
    }

    protected CommonApiException(ErrorCode errorCode, @NotNull String message) {
      super(message);

      this.status = errorCode.getStatus();
      this.code = errorCode.name();
    }

    protected CommonApiException(HttpStatus httpStatus, String message) {
      super(message);
      this.status = httpStatus;
      this.code = null;
    }

    protected CommonApiException(HttpStatus httpStatus, String code, String message) {
      super(message);
      this.status = httpStatus;
      this.code = code;
    }

    public static CommonApiException from(ErrorCode errorCode) {
      return new CommonApiException(errorCode);
    }

    public static CommonApiException of(HttpStatus httpStatus, String message) {
      return new CommonApiException(httpStatus, message);
    }

    public static CommonApiException of(HttpStatus httpStatus, String code, String message) {
      return new CommonApiException(httpStatus, code, message);
    }
  }