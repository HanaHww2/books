package com.book.data_set;

import com.book.data_set.naver.NaverBookResponse.NaverBookItem;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookInsertSqlBuilder {

  private static final DateTimeFormatter PUBDATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

  private BookInsertSqlBuilder() {}

  public static String buildInsertWithId(String table, long id, NaverBookItem i) {
    if (isBlank(i.isbn())) return null;

    String title = i.title();
    String subTitle = i.title();
    String author = i.author();
    Integer price = parsePrice(i.discount());
    String description = i.description();
    String image = i.image();
    String publisher = i.publisher();
    LocalDate published = parseDate(i.pubdate());

    return """
        INSERT INTO %s (id, isbn, title, sub_title, author, price, description, image, publisher, published, created_at, updated_at)
        VALUES (%d, %s, %s, %s, %s, %s, %s, %s, %s, %s, now(), now())
        ON CONFLICT (isbn) DO NOTHING;
        """.formatted(
        table,
        id,
        toSql(i.isbn()),
        toSql(title),
        toSql(subTitle),
        toSql(author),
        toSql(price),
        toPostgresELiteral(description),
        toSql(image),
        toSql(publisher),
        toSqlDate(published)
    );
  }

  public static boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }

  public static Integer parsePrice(String discount) {
    if (isBlank(discount)) return null;
    try { return Integer.valueOf(discount.trim()); }
    catch (NumberFormatException e) { return null; }
  }

  public static LocalDate parseDate(String yyyymmdd) {
    if (isBlank(yyyymmdd) || yyyymmdd.length() != 8) return null;
    try { return LocalDate.parse(yyyymmdd, PUBDATE_FMT); }
    catch (Exception e) { return null; }
  }

  public static String toSql(String s) {
    if (s == null) return "NULL";
    String esc = s.replace("'", "''");
    return "'" + esc + "'";
  }

  public static String toSql(Integer n) {
    return n == null ? "NULL" : n.toString();
  }

  public static String toPostgresELiteral(String s) {
    if (s == null) return "NULL";
    String esc = s
        .replace("\\", "\\\\")
        .replace("'", "''")
        .replace("\r\n", "\n").replace("\r", "\n")
        .replace("\n", "\\n");
    return "E'" + esc + "'";
  }

  public static String toSqlDate(LocalDate d) {
    return d == null ? "NULL" : "DATE '" + d + "'";
  }
}
