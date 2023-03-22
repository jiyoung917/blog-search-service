package com.example.blogsearchservice.service;

import com.example.blogsearchservice.dto.BlogSearchApiResult;
import com.example.blogsearchservice.dto.FetcherRequestContext;
import com.example.blogsearchservice.fetcher.KakaoBlogSearchFetcher;
import com.example.blogsearchservice.util.KakaoBlogSearchSort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class KakaoBlogSearchFetchHandler extends BlogFetchHandler {

  private final KakaoBlogSearchFetcher kakaoBlogSearchFetcher;

  @Override
  public BlogSearchApiResult handle(FetcherRequestContext context) {
    context.setSort(KakaoBlogSearchSort.getRealString(context.getSort()));
    try {
      return BlogSearchApiResult.from(kakaoBlogSearchFetcher.get(context));
    } catch (Exception e) {
      log.warn(e.getMessage());
      return doNext(context);
    }
  }

}
