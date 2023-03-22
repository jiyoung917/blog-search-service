package com.example.blogsearchservice.fetcher;

import org.springframework.stereotype.Component;

import com.example.blogsearchservice.client.NaverFeignClient;
import com.example.blogsearchservice.dto.FetcherRequestContext;
import com.example.blogsearchservice.dto.NaverBlogSearchResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class NaverBlogSearchFetcher {

  private final NaverFeignClient naverFeignClient;

  public NaverBlogSearchResult get(FetcherRequestContext context) {
    return naverFeignClient.getBlogSearchResult(
        context.getQuery(), context.getSort(), context.getPageNumber(), context.getPageSize());
  }

}
