package com.example.blogsearchservice.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.blogsearchservice.dto.BlogSearchApiResult.BlogSearchResultEntry;
import com.example.blogsearchservice.dto.HotKeywordInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BlogSearchFacade {

  private final BlogSearchService blogSearchService;

  public List<BlogSearchResultEntry> getBlogSearchResult(String query, Pageable pageable) throws InterruptedException {
    log.info("-- BlogSearchFacade process ---");

    int tryCount = 0;
    List<BlogSearchResultEntry> result = blogSearchService.getBlogSearchResult(query, pageable);
    while (true) {
      try {
        blogSearchService.upsertSearchKeywordInfo(query);
        return result;
      } catch (Exception e) {
        // retry
        log.warn(e.getMessage());
        Thread.sleep(1);
      } finally {
        tryCount++;
        log.info("try count: " + tryCount);
      }
    }
  }

  public List<HotKeywordInfo> getHotSearchKeywords() {
    return blogSearchService.getHotSearchKeywords();
  }

}
