package com.book.book.presentation;

import com.book.book.application.service.BookReadService;
import com.book.book.presentation.dto.request.GetBookDetailRequest;
import com.book.book.presentation.dto.request.SearchBookListRequest;
import com.book.book.presentation.dto.response.BookDetailResponse;
import com.book.book.presentation.dto.response.PopularKeywordListResponse;
import com.book.book.presentation.dto.response.SearchBookListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book API", description = "도서 조회 및 검색 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
@RestController
public class BookController {
  private final BookReadService bookReadService;

  @Operation(
      summary = "도서 상세 조회",
      description = "ISBN 또는 ID를 기준으로 도서 상세 정보를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 조회 조건"),
          @ApiResponse(responseCode = "404", description = "찾을 수 없는 도서 정보")
      }
  )
  @GetMapping
  public ResponseEntity<BookDetailResponse> getBookDetailInfo(
      @Validated @Parameter(description = "도서 상세 조회 요청 파라미터") GetBookDetailRequest req) {

    BookDetailResponse res = BookDetailResponse.from(bookReadService.findBookDetailInfoBy(req.toQuery()));
    return ResponseEntity.ok(res);
  }

  @Operation(
      summary = "도서 목록 검색",
      description = "검색 조건과 페이징 정보를 기준으로 도서 목록을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 조회 조건")
      }
  )
  @GetMapping("/list")
  public ResponseEntity<SearchBookListResponse> searchBookList(
      @PageableDefault @Parameter(description = "페이징 정보 (page, size)") Pageable pageable,
      @Validated @Parameter(description = "도서 검색 요청 파라미터") SearchBookListRequest req) {

    SearchBookListResponse res = SearchBookListResponse.from(bookReadService.searchBookListBy(req.toQuery(), pageable));
    return ResponseEntity.ok(res);
  }

  @Operation(
      summary = "인기 키워드 조회",
      description = "지난 1시간 동안 가장 많이 검색된 상위 10개 키워드를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공")
      }
  )
  @GetMapping("/popular-keywords")
  public ResponseEntity<PopularKeywordListResponse> getTop10KeywordsInPrevHour() {

    PopularKeywordListResponse res = PopularKeywordListResponse.from(bookReadService.getTop10KeywordListInfoInPrevHour());
    return ResponseEntity.ok(res);
  }
}
