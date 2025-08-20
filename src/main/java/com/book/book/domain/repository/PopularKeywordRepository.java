package com.book.book.domain.repository;

import com.book.book.application.service.search_strategy.SearchStrategyType;
import com.book.book.domain.info.PopularKeyword;
import java.util.List;

public interface PopularKeywordRepository {

  void record(String[] keywords, SearchStrategyType type);

  List<PopularKeyword> top10KeywordsInPrevHour();
}
