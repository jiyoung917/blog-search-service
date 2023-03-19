package com.example.blogsearchservice.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BlogSearchResult {

  private String title;

  private String contents;

  private LocalDateTime dateTime;

  private String thumbnail;

  private String url;

  private String blogName;

}
