package com.example.blogsearchservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.blogsearchservice.dto.BlogSearchResult;
import com.example.blogsearchservice.fetcher.KakaoBlogSearchFetcher;
import com.example.blogsearchservice.service.BlogSearchFacade;
import com.example.blogsearchservice.service.BlogSearchService;
import com.example.blogsearchservice.service.RedisService;

import io.github.bucket4j.Bucket;

@TestPropertySource("classpath:application.properties")
@ComponentScan(basePackages = "com.example.blogsearchservice")
@WebMvcTest(BlogSearchController.class)
public class BlogSearchControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Bucket bucket;

  @MockBean
  private BlogSearchFacade blogSearchFacade;

  @MockBean
  private BlogSearchService blogSearchService;

  @MockBean
  private KakaoBlogSearchFetcher kakaoBlogSearchFetcher;

  @MockBean
  private RedisService redisService;

  @Test()
  @DisplayName("1분당 50개의 토큰 생성으로 트래픽 제어를 한다.")
  void rateLimiting() throws Exception {
    List<BlogSearchResult> response = new ArrayList<>();
    given(blogSearchFacade.getBlogSearchResult(any(), any())).willReturn(response);

    AtomicInteger successCount = new AtomicInteger();
    AtomicInteger failedCount = new AtomicInteger();
    int numberOfExecute = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    CountDownLatch latch = new CountDownLatch(numberOfExecute);

    for (int i = 0; i < numberOfExecute; i++) {
      executorService.execute(() -> {
        try {
          int status = mockMvc.perform(get("http://localhost:8080/blog")
                  .param("query", "개발자")
                  .contentType(MediaType.APPLICATION_JSON)
                  .accept(MediaType.APPLICATION_JSON))
              .andReturn()
              .getResponse()
              .getStatus();

          if (status == 200) successCount.getAndIncrement();
        } catch (Exception e) {
          System.out.println(e.getMessage());
          if (e.getMessage().contains("ExceedTrafficException")) failedCount.getAndIncrement();
        }
        latch.countDown();
      });
    }
    latch.await();

    assertEquals(50, successCount.get());
    assertEquals(50, failedCount.get());
  }

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new BlogSearchController(bucket, blogSearchFacade))
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .build();
  }

}
