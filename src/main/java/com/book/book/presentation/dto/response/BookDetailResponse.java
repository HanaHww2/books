package com.book.book.presentation.dto.response;

import com.book.book.application.dto.info.BookDetailInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record BookDetailResponse(
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

  public static BookDetailResponse from(BookDetailInfo info) {
    return  BookDetailResponse.builder()
        .id(info.id())
        .isbn(info.isbn())
        .title(info.title())
        .subTitle(info.subTitle())
        .author(info.author())
        .price(info.price())
        .description(info.description())
        .image(info.image())
        .publisher(info.publisher())
        .published(info.published())
        .createdAt(info.createdAt())
        .updatedAt(info.updatedAt())
        .deletedAt(info.deletedAt())
        .build();
  }
}
