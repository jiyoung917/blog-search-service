package com.example.blogsearchservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.blogsearchservice.dto.NaverBlogSearchResult;

@FeignClient(name = "naverFeignClient", url = "${naver.blog-search.api.host}")
public interface NaverFeignClient {

  @GetMapping(value = "/v1/search/blog.json",
      headers = { "X-Naver-Client-Id=${naver.blog-search.api.client-id}", "X-Naver-Client-Secret=${naver.blog-search.api.client-secret}"},
      produces = "application/json", consumes = "application/json")
  NaverBlogSearchResult getBlogSearchResult(@RequestParam("query") String query,
      @RequestParam(value = "sort", defaultValue = "sim") String sort,
      @RequestParam(value = "start", defaultValue = "1") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size);


}
