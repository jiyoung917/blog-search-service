package com.example.blogsearchservice.service;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Component;

import com.example.blogsearchservice.fetcher.KakaoBlogSearchFetcher;
import com.example.blogsearchservice.fetcher.NaverBlogSearchFetcher;

import lombok.RequiredArgsConstructor;

@EnableFeignClients(basePackages = "com.example.blogsearchservice")
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
