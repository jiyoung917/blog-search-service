package com.example.blogsearchservice.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("classpath:application-api-test.properties")
@SpringBootTest
public class RedisServiceTest {

  @Autowired
  private RedisService redisService;

  @Test
  @DisplayName("redis에는 최대 10개만 저장된다")
  void storeUpTo10InRedis() {
    LinkedHashMap<String, Long> keywordInfos = new LinkedHashMap<>() {{
      put("개발자", 10L);
      put("Java", 30L);
      put("Spring", 15L);
      put("H2 DB", 15L);
      put("JPA", 50L);
      put("Redis", 5L);
      put("유튜브", 60L);
      put("맛집", 1L);
      put("여행", 20L);
      put("아이폰", 15L);
      put("애플페이", 15L);
    }};

    for (Entry<String, Long> keywordInfo : keywordInfos.entrySet()) {
      redisService.setValue(keywordInfo.getKey(), keywordInfo.getValue());
    }

    LinkedHashMap<String, Long> rank = redisService.getKeywordsRank(0, 10);
    assertEquals(rank.size(), 10);
    assertNull(rank.get("맛집"));
  }
}
