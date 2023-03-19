package com.example.blogsearchservice.fetcher;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Component;

import com.example.blogsearchservice.client.KakaoFeignClient;
import com.example.blogsearchservice.dto.KakaoBlogSearchResult;

import lombok.RequiredArgsConstructor;

@EnableFeignClients(basePackages = "com.example.blogsearchservice")
@RequiredArgsConstructor
@Component
public class KakaoBlogSearchFetcher {

  private final KakaoFeignClient kakaoFeignClient;

  public KakaoBlogSearchResult get(String query, String sort, int pageNumber) {
    return kakaoFeignClient.getBlogSearchResult(query, sort, pageNumber);
  }

}
