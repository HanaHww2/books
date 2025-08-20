package com.book.book.infrastructure.persistence.redis.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class PopularKeywordBucketUtil {

  private static final String SEARCH_POPULAR_HR = "search:popular:hr:";
  private static final DateTimeFormatter BUCKET_FMT =
      DateTimeFormatter.ofPattern("yyyyMMddHH");

  public static String currentHourBucketUtc() {
    return SEARCH_POPULAR_HR + LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC)
        .format(BUCKET_FMT);
  }

  public static String prevHourBucketUtc() {
    return SEARCH_POPULAR_HR + LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC)
        .minusHours(1)
        .format(BUCKET_FMT);
  }
}
