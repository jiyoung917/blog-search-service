package com.example.blogsearchservice.service;

import static org.mockito.BDDMockito.*;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import com.example.blogsearchservice.repository.SearchKeywordRepository;

@TestPropertySource("classpath:application-api-test.properties")
@SpringBootTest
public class BlogSearchServiceTest {

  @Autowired
  private BlogSearchService blogSearchService;

  @MockBean
  private RedisService redisService;

  @MockBean
  private SearchKeywordRepository searchKeywordRepository;

  @Test
  @DisplayName("redis가 비어있을 때 인기 검색어 조회 시 db로부터 가져온다")
  void givenRedisIsEmptyWhenGetHotSearchKeywordThenGetFromDb() {
    given(redisService.getKeywordsRank(0, 10)).willReturn(new LinkedHashMap<>());

    blogSearchService.getHotSearchKeywords();

    verify(searchKeywordRepository, times(1)).findTop10ByOrderBySearchCountDesc();
  }

  @Test
  @DisplayName("redis가 비어있지 않을 때 인기 검색어 조회 시 db로부터 가져오지 않는다")
  void givenRedisIsNotEmptyWhenGetHotSearchKeywordThenGetFromDb() {
    LinkedHashMap<String, Long> keywordsRank = new LinkedHashMap<>() {{
      put("개발자", 1L);
    }};
    given(redisService.getKeywordsRank(0, 10)).willReturn(keywordsRank);

    blogSearchService.getHotSearchKeywords();

    verify(searchKeywordRepository, times(0)).findTop10ByOrderBySearchCountDesc();
  }
}
