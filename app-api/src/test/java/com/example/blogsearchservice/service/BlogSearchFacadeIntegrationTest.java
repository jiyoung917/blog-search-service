package com.example.blogsearchservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import com.example.blogsearchservice.client.KakaoFeignClient;
import com.example.blogsearchservice.dto.FetcherRequestContext;
import com.example.blogsearchservice.dto.HotKeywordInfo;
import com.example.blogsearchservice.dto.KakaoBlogSearchResult;
import com.example.blogsearchservice.fetcher.KakaoBlogSearchFetcher;

@TestPropertySource("classpath:application-api-test.properties")
@SpringBootTest
public class BlogSearchFacadeIntegrationTest {

  @Autowired
  private BlogSearchFacade blogSearchFacade;

  @MockBean
  private KakaoBlogSearchFetcher kakaoBlogSearchFetcher;

  @MockBean
  private KakaoFeignClient kakaoFeignClient;

  @MockBean
  private RedisService redisService;

  @Test
  @DisplayName("동시성 이슈가 발생하지 않는 블로그 검색 조회로 인한 검색 수 증가 테스트")
  void searchCountIncrementTest() throws InterruptedException {
    String query = "개발자";
    String sort = "accu";
    KakaoBlogSearchResult kakaoBlogSearchResult = new KakaoBlogSearchResult();
    given(kakaoBlogSearchFetcher.get(new FetcherRequestContext(query, sort, 1))).willReturn(kakaoBlogSearchResult);

    int numberOfExecute = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    CountDownLatch latch = new CountDownLatch(numberOfExecute);

    for (int i = 0; i < numberOfExecute; i++) {
      executorService.execute(() -> {
        try {
          blogSearchFacade.getBlogSearchResult(query, PageRequest.of(1, 10, Sort.by(sort)));
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
        latch.countDown();
      });
    }
    latch.await();

    List<HotKeywordInfo> hotSearchKeywords = blogSearchFacade.getHotSearchKeywords();

    assertEquals(100, hotSearchKeywords.get(0).getSearchCount());
  }

}
