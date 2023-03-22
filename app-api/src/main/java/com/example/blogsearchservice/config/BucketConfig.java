package com.example.blogsearchservice.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Configuration
public class BucketConfig {

  @Value("${bucket.capacity}")
  private long capacity;

  @Value("${bucket.tokens}")
  private long tokens;

  @Value("${bucket.tokens-create.period}")
  private long period;

  @Bean
  public Bucket bucket() {
    Bandwidth limit = Bandwidth.classic(capacity, Refill.intervally(tokens, Duration.ofSeconds(period)));
    return Bucket.builder()
        .addLimit(limit)
        .build();
  }

}
