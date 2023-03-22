package com.example.blogsearchservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.blogsearchservice.dto.KakaoBlogSearchResult;

@FeignClient(name = "kakaoFeignClient", url = "${kakao.blog-search.api.host}")
public interface KakaoFeignClient {

  @GetMapping(value = "/v2/search/blog", headers = "Authorization=${kakao.blog-search.api.key}",
      produces = "application/json", consumes = "application/json")
  KakaoBlogSearchResult getBlogSearchResult(@RequestParam("query") String query,
      @RequestParam(value = "sort", defaultValue = "accuracy") String sort,
      @RequestParam(value = "page", defaultValue = "1") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size);

}
