package com.example.blogsearchservice.fetcher;

import org.springframework.stereotype.Component;

import com.example.blogsearchservice.client.KakaoFeignClient;
import com.example.blogsearchservice.dto.FetcherRequestContext;
import com.example.blogsearchservice.dto.KakaoBlogSearchResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class KakaoBlogSearchFetcher {

  private final KakaoFeignClient kakaoFeignClient;

  public KakaoBlogSearchResult get(FetcherRequestContext context) {
    return kakaoFeignClient.getBlogSearchResult(context.getQuery(), context.getSort(), context.getPageNumber());
  }

}
