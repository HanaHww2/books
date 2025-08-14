package com.book.book.domain.entity.vo;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublishInfo {

  private String publisher;

  private LocalDate published;
}
