package com.example.blogsearchservice.service;

import org.springframework.stereotype.Component;

import com.example.blogsearchservice.fetcher.KakaoBlogSearchFetcher;
import com.example.blogsearchservice.fetcher.NaverBlogSearchFetcher;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FetcherFactory {

  private final KakaoBlogSearchFetcher kakaoBlogSearchFetcher;

  private final NaverBlogSearchFetcher naverBlogSearchFetcher;

  public BlogFetchHandler getHandler() {
    BlogFetchHandler handler = new KakaoBlogSearchFetchHandler(kakaoBlogSearchFetcher);
    handler.linkWith(new NaverBlogSearchFetchHandler(naverBlogSearchFetcher));
    return handler;
  }

}
