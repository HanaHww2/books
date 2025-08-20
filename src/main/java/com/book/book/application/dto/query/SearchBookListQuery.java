package com.book.book.application.dto.query;

import lombok.Builder;

@Builder
public record SearchBookListQuery(
    String keyword
) {

}
