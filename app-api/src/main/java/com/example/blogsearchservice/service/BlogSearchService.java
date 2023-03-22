package com.example.blogsearchservice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.blogsearchservice.domain.SearchKeyword;
import com.example.blogsearchservice.dto.BlogSearchApiResult;
import com.example.blogsearchservice.dto.BlogSearchApiResult.BlogSearchResultEntry;
import com.example.blogsearchservice.dto.FetcherRequestContext;
import com.example.blogsearchservice.dto.HotKeywordInfo;
import com.example.blogsearchservice.repository.SearchKeywordRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BlogSearchService {

  private final SearchKeywordRepository searchKeywordRepository;

  private final FetcherFactory fetcherFactory;

  private final RedisService redisService;

  public List<BlogSearchResultEntry> getBlogSearchResult(String query, Pageable pageable) {
    log.info("-- Start getting blog search result --");

    if (query == null || query.isEmpty()) throw new IllegalArgumentException("Query parameter required");
    if (pageable.getPageNumber() < 1) throw new IllegalArgumentException("Page number cannot be less than 1");

    FetcherRequestContext fetcherRequestContext =
        new FetcherRequestContext(query, pageable.getSort().toString(), pageable.getPageNumber(), pageable.getPageSize());
    BlogSearchApiResult blogSearchApiResult = fetcherFactory.getHandler().handle(fetcherRequestContext);

    return blogSearchApiResult != null ? blogSearchApiResult.getEntries() : Collections.emptyList();
  }

  @Transactional
  public void upsertSearchKeywordInfo(String query) {
    log.info("-- Start upserting search keyword info ---");

    SearchKeyword searchKeyword = searchKeywordRepository.findByWithOptimisticLock(query);
    long searchCount = 1L;
    if (searchKeyword == null)  {
      // db에 들어간 첫 검색어일 경우
      searchKeyword = new SearchKeyword(query);
    } else {
      searchCount = searchKeyword.getSearchCount() + 1;
      searchKeyword.setSearchCount(searchCount);
    }
    searchKeywordRepository.save(searchKeyword);
    redisService.setValue(query, searchCount);
  }

  private synchronized void insert(String query, long searchCount) {
    SearchKeyword searchKeyword = new SearchKeyword(query);
    searchKeywordRepository.save(searchKeyword);
    redisService.setValue(query, searchCount);
  }

  public List<HotKeywordInfo> getHotSearchKeywords() {
    List<HotKeywordInfo> hotKeywords = new ArrayList<>();
    LinkedHashMap<String, Long> keywordRank = redisService.getKeywordsRank(0, 10);
    for (Entry<String, Long> keywordRankSet : keywordRank.entrySet()) {
      hotKeywords.add(new HotKeywordInfo(keywordRankSet.getKey(), keywordRankSet.getValue()));
    }

    if (hotKeywords.isEmpty()) {
      List<SearchKeyword> keywords = searchKeywordRepository.findTop10ByOrderBySearchCountDesc();
      for (SearchKeyword keyword : keywords) {
        hotKeywords.add(new HotKeywordInfo(keyword.getKeyword(), keyword.getSearchCount()));
        redisService.setValue(keyword.getKeyword(), keyword.getSearchCount());
      }
    }

    return hotKeywords;
  }

}
