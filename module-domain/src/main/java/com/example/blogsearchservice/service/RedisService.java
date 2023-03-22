package com.example.blogsearchservice.service;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {

  private final RedisTemplate<String, String> redisTemplate;
  private ZSetOperations<String, String> zSetOps;

  private static final String KEY = "blogHotSearchKeyword";

  @PostConstruct
  public void init() {
    zSetOps = redisTemplate.opsForZSet();
  }

  public void setValue(String keyword, Long searchCount) {
    log.info("-- Set search keyword and count in redis --");

    zSetOps.add(KEY, keyword, searchCount);
    if (zSetOps.zCard(KEY) > 10) zSetOps.removeRange(KEY, 0, 0);
  }

  public LinkedHashMap<String, Long> getKeywordsRank(int startIndex, int endIndex) {
    log.info("-- Get search keyword ranking from redis --");

    Set<String> rankReverseSet = zSetOps.reverseRange(KEY, startIndex, endIndex);
    Iterator<String> iter = rankReverseSet.iterator();
    LinkedHashMap<String, Long> list = new LinkedHashMap<>();

    while (iter.hasNext()) {
      String keyword = iter.next();
      list.put(keyword, zSetOps.score(KEY, keyword).longValue());
    }

    return list;
  }

}
