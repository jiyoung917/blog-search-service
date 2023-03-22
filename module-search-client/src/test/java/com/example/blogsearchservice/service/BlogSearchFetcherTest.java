package com.example.blogsearchservice.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.blogsearchservice.dto.FetcherRequestContext;
import com.example.blogsearchservice.fetcher.KakaoBlogSearchFetcher;
import com.example.blogsearchservice.fetcher.NaverBlogSearchFetcher;

@ExtendWith(MockitoExtension.class)
public class BlogSearchFetcherTest {

  @Mock
  private KakaoBlogSearchFetcher kakaoBlogSearchFetcher;

  @Mock
  private NaverBlogSearchFetcher naverBlogSearchFetcher;

  @InjectMocks
  private FetcherFactory fetcherFactory;

  @Test
  void test() {
    FetcherRequestContext context = new FetcherRequestContext("개발자", "accu", 1, 10);
    doThrow(new RuntimeException("API call failed"))
      .when(kakaoBlogSearchFetcher).get(context);

    fetcherFactory.getHandler().handle(context);

    verify(naverBlogSearchFetcher, times(1)).get(context);
  }

}
