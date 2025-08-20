package com.book.book.application.dto.info;

import com.book.book.domain.entity.Book;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Builder
public record SearchBookListInfo(
    PageInfo pageInfo,
    List<SearchBookResponse> books,
    String searchQuery,
    SearchMetaData searchMetaData
) {

  public static SearchBookListInfo of(Page<Book> bookPage, String searchQuery, String strategy, Long executionTime) {
    Pageable pageable = bookPage.getPageable();

    return SearchBookListInfo.builder()
        .pageInfo(PageInfo.builder()
            .currentPage(pageable.getPageNumber() + 1)
            .pageSize(pageable.getPageSize())
            .totalPages(bookPage.getTotalPages())
            .totalElements(bookPage.getTotalElements())
            .build()
        )
        .books(bookPage.getContent().stream()
            .map(SearchBookResponse::from)
            .toList()
        )
        .searchQuery(searchQuery)
        .searchMetaData(SearchMetaData.builder()
            .strategy(strategy)
            .executionTime(executionTime)
            .build()
        )
        .build();
  }

  @Builder
  public record SearchBookResponse(
      Long id,
      String title,
      String subTitle,
      String author,
      String image,
      String isbn,
      LocalDate published
  ) {

    public static SearchBookResponse from(Book book) {
      return SearchBookResponse.builder()
          .id(book.getId())
          .title(book.getBaseInfo().getTitle())
          .subTitle(book.getBaseInfo().getSubTitle())
          .author(book.getBaseInfo().getAuthor())
          .image(book.getExtraInfo().getImage())
          .isbn(book.getIsbn())
          .published(book.getPublishInfo().getPublished())
          .build();
    }
  }

  @Builder
  public record PageInfo(
      Integer currentPage,
      Integer pageSize,
      Integer totalPages,
      Long totalElements
  ) {}

  @Builder
  public record SearchMetaData(
      Long executionTime,
      String strategy
  ) {}
}
