package com.book.book.infrastructure.persistence.redis;

import com.book.book.application.service.search_strategy.SearchStrategyType;
import com.book.book.domain.info.PopularKeyword;
import com.book.book.domain.repository.PopularKeywordRepository;
import com.book.book.infrastructure.persistence.redis.utils.PopularKeywordBucketUtil;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PopularKeywordRedisRepository implements PopularKeywordRepository {

  private static final int RETENTION_DAYS = 7;
  private final StringRedisTemplate stringRedisTemplate;

  @Override
  public void record(String[] keywords, SearchStrategyType type) {

    String bucket = PopularKeywordBucketUtil.currentHourBucketUtc();
    double w = (type == SearchStrategyType.OR_OPERATION) ? (1.0 / keywords.length) : 1.0;

    for (String keyword : keywords) {
      stringRedisTemplate.opsForZSet().incrementScore(bucket, keyword, w);
    }

    stringRedisTemplate.expire(bucket, Duration.ofDays(RETENTION_DAYS));
  }

  @Override
  public List<PopularKeyword> top10KeywordsInPrevHour() {

    String prevBucket = PopularKeywordBucketUtil.prevHourBucketUtc();
    Set<TypedTuple<String>> set =
        stringRedisTemplate.opsForZSet().reverseRangeWithScores(prevBucket, 0, 9);

    if (set == null || set.isEmpty()) return List.of();

    List<PopularKeyword> result = new ArrayList<>(set.size());
    double prevScore = Double.NaN;
    int rank = 0;

    for (TypedTuple<String> t : set) {
      double score = Optional.ofNullable(t.getScore()).orElse(0.0);
      if (result.isEmpty()) {
        rank = 1;
      } else if (Double.compare(score, prevScore) != 0) {
        rank += 1;
      }

      result.add(PopularKeyword.of(t.getValue(), score, rank));
      prevScore = score;
    }
    return result;
  }
}
