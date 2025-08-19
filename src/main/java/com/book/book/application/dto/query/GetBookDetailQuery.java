package com.book.book.application.dto.query;

import lombok.Builder;

@Builder
public record GetBookDetailQuery(
    String isbn,
    Long id
) {
}
