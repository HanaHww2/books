package com.book.book.domain.entity;

import com.book.book.domain.entity.vo.BaseInfo;
import com.book.book.domain.entity.vo.ExtraInfo;
import com.book.book.domain.entity.vo.PublishInfo;
import com.book.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name="books")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {

  @Id
  @SequenceGenerator(
      name = "books_seq_gen",
      sequenceName = "books_seq",
      allocationSize = 50
  )
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="books_seq_gen")
  private Long id;

  @Column(unique = true)
  private String isbn;

  @Embedded
  private BaseInfo baseInfo;

  @Embedded
  private ExtraInfo extraInfo;

  @Embedded
  private PublishInfo publishInfo;

  @Builder
  public Book(String isbn, BaseInfo baseInfo, ExtraInfo extraInfo, PublishInfo publishInfo) {
    this.isbn = isbn;
    this.baseInfo = baseInfo;
    this.extraInfo = extraInfo;
    this.publishInfo = publishInfo;
  }
}
