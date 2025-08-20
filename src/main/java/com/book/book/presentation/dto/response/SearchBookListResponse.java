package com.book.book.presentation.dto.response;

import com.book.book.application.dto.info.SearchBookListInfo;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record SearchBookListResponse(
    PageInfo pageInfo,
    List<SearchBookResponse> books,
    String searchQuery,
    SearchMetaData searchMetaData
) {


  public static SearchBookListResponse from(SearchBookListInfo searchBookListInfo) {
    return SearchBookListResponse.builder()
        .pageInfo(PageInfo.from(searchBookListInfo.pageInfo()))
        .books(searchBookListInfo.books().stream().map(SearchBookResponse::from).toList())
        .searchQuery(searchBookListInfo.searchQuery())
        .searchMetaData(SearchMetaData.from(searchBookListInfo.searchMetaData()))
        .build();
  }

  @Builder
  record PageInfo(
      Integer currentPage,
      Integer pageSize,
      Integer totalPages,
      Long totalElements
  ) {

    public static PageInfo from(SearchBookListInfo.PageInfo pageInfo) {
      return PageInfo.builder()
          .currentPage(pageInfo.currentPage())
          .pageSize(pageInfo.pageSize())
          .totalPages(pageInfo.totalPages())
          .totalElements(pageInfo.totalElements())
          .build();
    }
  }

  @Builder
  record SearchBookResponse(
      Long id,
      String title,
      String subTitle,
      String image,
      String author,
      String isbn,
      LocalDate published
  ) {

    public static SearchBookResponse from(SearchBookListInfo.SearchBookResponse searchBookResponse) {
      return SearchBookResponse.builder()
          .id(searchBookResponse.id())
          .title(searchBookResponse.title())
          .subTitle(searchBookResponse.subTitle())
          .image(searchBookResponse.image())
          .author(searchBookResponse.author())
          .isbn(searchBookResponse.isbn())
          .published(searchBookResponse.published())
          .build();
    }
  }

  @Builder
  record SearchMetaData(
      Long executionTime,
      String strategy
  ) {

    public static SearchMetaData from(SearchBookListInfo.SearchMetaData searchMetaData) {
      return SearchMetaData.builder()
          .strategy(searchMetaData.strategy())
          .executionTime(searchMetaData.executionTime())
          .build();
    }
  }
}
