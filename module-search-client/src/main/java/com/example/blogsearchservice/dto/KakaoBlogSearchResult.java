package com.example.blogsearchservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class KakaoBlogSearchResult {

  @JsonProperty("documents")
  private List<KakaoBlogSearchEntry> entries;

  @Getter
  public static class KakaoBlogSearchEntry {

    private String title;

    private String contents;

    @JsonProperty("datetime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private LocalDateTime postDateTime;

    private String thumbnail;

    private String url;

    @JsonProperty("blogname")
    private String blogName;

  }

}
