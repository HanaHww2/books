package com.book.book.presentation;

import com.book.book.application.service.BookReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BookController {
  private final BookReadService bookReadService;
}
