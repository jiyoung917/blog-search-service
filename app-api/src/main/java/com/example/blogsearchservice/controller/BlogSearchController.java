package com.example.blogsearchservice.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogsearchservice.dto.BlogSearchResult;
import com.example.blogsearchservice.dto.HotKeywordInfo;
import com.example.blogsearchservice.service.BlogSearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BlogSearchController {

  private final BlogSearchService blogSearchService;

  @GetMapping("/blog")
  public List<BlogSearchResult> getBlogSearchResult(@RequestParam String query,
      @PageableDefault(page = 1, sort = "accu") Pageable pageable) {
    log.info("-- blog search controller --");
    return blogSearchService.getBlogSearchResult(query, pageable);
  }

  @GetMapping("/hot-keyword")
  public List<HotKeywordInfo> getHotKeywords() {
    return blogSearchService.getHotKeywords();
  }

}
