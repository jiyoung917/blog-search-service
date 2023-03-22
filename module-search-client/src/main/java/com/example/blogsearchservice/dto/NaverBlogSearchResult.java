package com.example.blogsearchservice.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class NaverBlogSearchResult {

  @JsonProperty("items")
  private List<NaverBlogSearchEntry> entries;

  @Getter
  public static class NaverBlogSearchEntry {

    private String title;

    @JsonProperty("description")
    private String contents;

    @JsonProperty("postdate")
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate postDate;

    @JsonProperty("link")
    private String url;

    @JsonProperty("bloggername")
    private String blogName;

  }

}
