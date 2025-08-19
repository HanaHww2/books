package com.book.book.domain.entity.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExtraInfo {

  private Integer price;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(length = 500)
  private String image;

  @Builder
  private ExtraInfo(Integer price, String description, String image) {
    this.price = price;
    this.description = description;
    this.image = image;
  }
}
