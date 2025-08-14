package com.book.data_set;

import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class SequenceIdFetcher {

  private final JdbcTemplate jdbcTemplate;

  public SequenceIdFetcher(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Long> fetchNextIds(String qualifiedSeqName, int count) {
    String sql = """
      SELECT nextval(?) AS id
      FROM generate_series(1, ?)
    """;
    return jdbcTemplate.query(
        con -> {
          var ps = con.prepareStatement(sql);
          ps.setString(1, qualifiedSeqName);
          ps.setInt(2, count);
          return ps;
        },
        (rs, rowNum) -> rs.getLong("id")
    );
  }

}