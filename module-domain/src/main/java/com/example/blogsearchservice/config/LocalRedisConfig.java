package com.example.blogsearchservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import redis.embedded.RedisServer;

@ConditionalOnProperty(value = "localRedisConfig", havingValue = "domain")
@Configuration
public class LocalRedisConfig {

  @Value("${spring.data.redis.port}")
  private int port;

  private RedisServer redisServer;

  @PostConstruct
  public void redisServer() {
    redisServer = new RedisServer(port);
    redisServer.start();
  }

  @PreDestroy
  public void stopRedis() {
    if (redisServer != null) {
      redisServer.stop();
    }
  }

}
