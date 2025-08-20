package com.book.book.fixture;

import com.book.book.application.dto.info.SearchBookListInfo;
import com.book.book.application.dto.info.SearchBookListInfo.PageInfo;
import com.book.book.application.dto.info.SearchBookListInfo.SearchBookInfo;
import com.book.book.application.dto.info.SearchBookListInfo.SearchMetaData;
import java.util.List;

public class SearchBookListInfoFixture {

  public static SearchBookListInfo create() {
    return SearchBookListInfo.builder()
        .searchQuery("test")
        .searchMetaData(SearchMetaData.builder()
            .strategy("BASIC_OPERATION")
            .executionTime(130L)
            .build())
        .pageInfo(PageInfo.builder()
            .currentPage(1)
            .pageSize(10)
            .totalPages(1)
            .totalElements(3L)
            .build())
        .books(
            List.of(createSearchBookInfo("test1", "123456789123"),
                createSearchBookInfo("test2", "123456789124"),
                createSearchBookInfo("test3", "123456789125"))
        )
        .build();
  }

  public static SearchBookInfo createSearchBookInfo(String title, String isbn) {
    return SearchBookInfo.builder()
        .title(title)
        .isbn(isbn)
        .build();
  }
}
