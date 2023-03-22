package com.example.blogsearchservice.service;

import com.example.blogsearchservice.dto.BlogSearchApiResult;
import com.example.blogsearchservice.dto.FetcherRequestContext;

public abstract class BlogFetchHandler {

  private BlogFetchHandler next;

  public BlogFetchHandler linkWith(final BlogFetchHandler next) {
    this.next = next;
    return next;
  }

  public abstract BlogSearchApiResult handle(FetcherRequestContext context);

  public BlogSearchApiResult doNext(FetcherRequestContext context) {
    if (next != null) return next.handle(context);
    return null;
  }

}
