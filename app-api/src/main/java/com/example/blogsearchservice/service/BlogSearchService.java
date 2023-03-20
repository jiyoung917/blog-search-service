package com.example.blogsearchservice.service;

import java.util.ArrayList;
import java.util.List;

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

  private final KakaoBlogSearchFetcher kakaoBlogSearchFetcher;

  private final SearchKeywordRepository searchKeywordRepository;

  @Transactional
  public List<BlogSearchResult> getBlogSearchResult(String query, Pageable pageable) {
    log.info("-- blog search service --");
    if (query == null || query.isEmpty()) throw new IllegalArgumentException("Query parameter required");
    if (pageable.getPageNumber() < 1) throw new IllegalArgumentException("Page number cannot be less than 1");

    SearchKeyword searchKeyword = searchKeywordRepository.findByWithPessimisticLock(query);
    if (searchKeyword == null) searchKeyword = new SearchKeyword(query);
    else searchKeyword.setSearchCount(searchKeyword.getSearchCount() + 1);
    searchKeywordRepository.save(searchKeyword);

    KakaoBlogSearchResult kakaoBlogSearchResult = kakaoBlogSearchFetcher.get(query,
        KakaoBlogSearchSort.getRealString(pageable.getSort().toString()),
        pageable.getPageNumber());

    List<BlogSearchResult> entries = new ArrayList<>();
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

    return entries;
  }

  public List<HotKeywordInfo> getHotKeywords() {
    List<HotKeywordInfo> hotKeywords = new ArrayList<>();
    List<SearchKeyword> keywords = searchKeywordRepository.findTop10ByOrderBySearchCountDesc();
    for (SearchKeyword keyword : keywords) {
      hotKeywords.add(new HotKeywordInfo(keyword.getKeyword(), keyword.getSearchCount()));
    }

    return hotKeywords;
  }

}
