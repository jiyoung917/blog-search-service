package com.example.blogsearchservice.service;

import com.example.blogsearchservice.dto.BlogSearchApiResult;
import com.example.blogsearchservice.dto.FetcherRequestContext;
import com.example.blogsearchservice.fetcher.NaverBlogSearchFetcher;
import com.example.blogsearchservice.util.NaverBlogSearchSort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NaverBlogSearchFetchHandler extends BlogFetchHandler {

  private final NaverBlogSearchFetcher naverBlogSearchFetcher;

  @Override
  public BlogSearchApiResult handle(FetcherRequestContext context) {
    context.setSort(NaverBlogSearchSort.getRealString(context.getSort()));
    try {
      return BlogSearchApiResult.from(naverBlogSearchFetcher.get(context));
    } catch (Exception e) {
      log.warn(e.getMessage());
      return doNext(context);
    }
  }

}
