package com.book.book.application.dto.info;

import com.book.book.domain.entity.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record BookDetailInfo(
    Long id,
    String isbn,
    String title,
    String subTitle,
    String author,
    Integer price,
    String description,
    String image,
    String publisher,
    LocalDate published,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt
) {

  public static BookDetailInfo from(Book book) {

    return BookDetailInfo.builder()
        .id(book.getId())
        .isbn(book.getIsbn())
        .title(book.getBaseInfo().getTitle())
        .subTitle(book.getBaseInfo().getSubTitle())
        .author(book.getBaseInfo().getAuthor())
        .price(book.getExtraInfo().getPrice())
        .description(book.getExtraInfo().getDescription())
        .image(book.getExtraInfo().getImage())
        .publisher(book.getPublishInfo().getPublisher())
        .published(book.getPublishInfo().getPublished())
        .createdAt(book.getCreatedAt())
        .updatedAt(book.getUpdatedAt())
        .deletedAt(book.getDeletedAt())
        .build();
  }
}
