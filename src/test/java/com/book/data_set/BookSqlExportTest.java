package com.book.data_set;

import com.book.data_set.naver.NaverBookResponse;
import com.book.data_set.naver.NaverBookResponse.NaverBookItem;
import com.book.data_set.naver.NaverBookWebClient;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Disabled("sql 파일을 만들기 위해서 사용하는 코드입니다. naver api의 클라이언트 아이디, 시크릿이 필요합니다.")
@ActiveProfiles("test")
@SpringBootTest
class BookSqlExportTest {

  private final NaverBookWebClient naver;
  private final SequenceIdFetcher idFetcher;

  BookSqlExportTest(NaverBookWebClient naver, SequenceIdFetcher idFetcher) {
    this.naver = naver;
    this.idFetcher = idFetcher;
  }

  @Test
  void exportInsertSqlFromNaver() throws Exception {
    String query = "개발";
    int display = 100;
    int start = 1;
    String table = "books";
    String seq = "books_seq";

    NaverBookResponse resp = naver.search(query, display, start);
    if (resp == null || resp.items() == null || resp.items().isEmpty()) return;

    List<NaverBookItem> items = resp.items();
    List<Long> ids = idFetcher.fetchNextIds(seq, items.size());

    List<String> lines = new ArrayList<>();
    for (int idx = 0; idx < items.size(); idx++) {
      long id = ids.get(idx);
      String sql = BookInsertSqlBuilder.buildInsertWithId(table, id, items.get(idx));
      if (sql != null) lines.add(sql);
    }

    File out = new File("src/main/resources/db/data.sql");
    SqlFileWriter.writeLines(out, lines);
    System.out.println("Wrote SQL to: " + out.getAbsolutePath());
  }
}
