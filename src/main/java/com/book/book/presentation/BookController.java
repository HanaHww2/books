package com.book.book.presentation;

import com.book.book.application.service.BookReadService;
import com.book.book.presentation.dto.request.GetBookDetailRequest;
import com.book.book.presentation.dto.response.BookDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
@RestController
public class BookController {
  private final BookReadService bookReadService;

  @GetMapping
  public ResponseEntity<BookDetailResponse> getBookDetailInfo(
      @Validated GetBookDetailRequest req) {

    BookDetailResponse res = BookDetailResponse.from(bookReadService.findBookDetailInfoBy(req.toQuery()));
    return ResponseEntity.ok(res);
  }
}
