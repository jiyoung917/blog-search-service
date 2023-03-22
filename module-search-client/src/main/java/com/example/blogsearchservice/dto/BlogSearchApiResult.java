package com.example.blogsearchservice.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.blogsearchservice.dto.KakaoBlogSearchResult.KakaoBlogSearchEntry;
import com.example.blogsearchservice.dto.NaverBlogSearchResult.NaverBlogSearchEntry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlogSearchApiResult {

  private List<BlogSearchResultEntry> entries;

  public static BlogSearchApiResult from(Object result) {
    List<BlogSearchResultEntry> entries = new ArrayList<>();
    if (result instanceof KakaoBlogSearchResult) {
      for (KakaoBlogSearchEntry kakaoEntry : ((KakaoBlogSearchResult) result).getEntries()) {
        entries.add(BlogSearchResultEntry.builder()
                .title(kakaoEntry.getTitle())
                .contents(kakaoEntry.getContents())
                .dateTime(kakaoEntry.getPostDateTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")))
                .thumbnail(kakaoEntry.getThumbnail())
                .url(kakaoEntry.getUrl())
                .blogName(kakaoEntry.getBlogName())
            .build());
      }
    } else if (result instanceof NaverBlogSearchResult) {
      for (NaverBlogSearchEntry naverEntry : ((NaverBlogSearchResult) result).getEntries()) {
        entries.add(BlogSearchResultEntry.builder()
            .title(naverEntry.getTitle())
            .contents(naverEntry.getContents())
            .dateTime(naverEntry.getPostDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
            .thumbnail(null)
            .url(naverEntry.getUrl())
            .blogName(naverEntry.getBlogName())
            .build());
      }
    }
    return new BlogSearchApiResult(entries);
  }

  @Getter
  @Builder
  public static class BlogSearchResultEntry {

    private String title;

    private String contents;

    private String dateTime;

    private String thumbnail;

    private String url;

    private String blogName;

  }

}
