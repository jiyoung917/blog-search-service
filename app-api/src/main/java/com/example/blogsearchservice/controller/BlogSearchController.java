package com.example.blogsearchservice.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogsearchservice.dto.BlogSearchResult;
import com.example.blogsearchservice.service.BlogSearchService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BlogSearchController {

  private final BlogSearchService blogSearchService;

  @GetMapping("/blog")
  public List<BlogSearchResult> getBlogSearchResult(@RequestParam String query,
      @PageableDefault(page = 1, size = 10, sort = "accu") Pageable pageable) {
    return blogSearchService.getBlogSearchResult(query, pageable);
  }

}
