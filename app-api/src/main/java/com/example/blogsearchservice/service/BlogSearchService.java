package com.example.blogsearchservice.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.blogsearchservice.domain.SearchKeyword;
import com.example.blogsearchservice.dto.BlogSearchResult;
import com.example.blogsearchservice.dto.HotKeywordInfo;
import com.example.blogsearchservice.dto.KakaoBlogSearchResult;
import com.example.blogsearchservice.dto.KakaoBlogSearchResult.KakaoBlogSearchEntry;
import com.example.blogsearchservice.fetcher.KakaoBlogSearchFetcher;
import com.example.blogsearchservice.repository.SearchKeywordRepository;
import com.example.blogsearchservice.util.KakaoBlogSearchSort;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BlogSearchService {

  private final SearchKeywordRepository searchKeywordRepository;

  private final KakaoBlogSearchFetcher kakaoBlogSearchFetcher;

  private final RedisService redisService;

  public List<BlogSearchResult> getBlogSearchResult(String query, Pageable pageable) {
    log.info("-- Start getting blog search result --");

    if (query == null || query.isEmpty()) throw new IllegalArgumentException("Query parameter required");
    if (pageable.getPageNumber() < 1) throw new IllegalArgumentException("Page number cannot be less than 1");

    KakaoBlogSearchResult kakaoBlogSearchResult = kakaoBlogSearchFetcher.get(query,
        KakaoBlogSearchSort.getRealString(pageable.getSort().toString()),
        pageable.getPageNumber());

    List<BlogSearchResult> entries = new ArrayList<>();
    if (kakaoBlogSearchResult != null) {
      for (KakaoBlogSearchEntry entry : kakaoBlogSearchResult.getDocuments()) {
        entries.add(BlogSearchResult.builder()
            .title(entry.getTitle())
            .contents(entry.getContents())
            .dateTime(entry.getDateTime())
            .thumbnail(entry.getThumbnail())
            .url(entry.getUrl())
            .blogName(entry.getBlogName())
            .build());
      }
    }

    return entries;
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
