package com.example.blogsearchservice.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogsearchservice.dto.BlogSearchResult;
import com.example.blogsearchservice.dto.HotKeywordInfo;
import com.example.blogsearchservice.exception.ExceedTrafficException;
import com.example.blogsearchservice.service.BlogSearchFacade;
import com.example.blogsearchservice.service.BlogSearchService;

import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BlogSearchController {

  private final Bucket bucket;

  private final BlogSearchFacade blogSearchFacade;

  @GetMapping("/blog")
  public List<BlogSearchResult> getBlogSearchResult(@RequestParam String query,
      @PageableDefault(page = 1) @SortDefault(sort = "accu") Pageable pageable) throws InterruptedException {
    log.info("Available tokens : " + bucket.getAvailableTokens());

    if (bucket.tryConsume(1)) {
      return blogSearchFacade.getBlogSearchResult(query, pageable);
    }

    throw new ExceedTrafficException();
  }

  @GetMapping("/hot-keyword")
  public List<HotKeywordInfo> getHotKeywords() {
    log.info("Available tokens : " + bucket.getAvailableTokens());

    if (bucket.tryConsume(1)) {
      return blogSearchFacade.getHotSearchKeywords();
    }

    throw new ExceedTrafficException();
  }

}
