package com.example.blogsearchservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.blogsearchservice.dto.BlogSearchResult;
import com.example.blogsearchservice.dto.KakaoBlogSearchResult;
import com.example.blogsearchservice.dto.KakaoBlogSearchResult.KakaoBlogSearchEntry;
import com.example.blogsearchservice.fetcher.KakaoBlogSearchFetcher;
import com.example.blogsearchservice.util.KakaoBlogSearchSort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BlogSearchService {

  private final KakaoBlogSearchFetcher kakaoBlogSearchFetcher;

  public List<BlogSearchResult> getBlogSearchResult(String query, Pageable pageable) {
    KakaoBlogSearchResult kakaoBlogSearchResult = kakaoBlogSearchFetcher.get(query,
        KakaoBlogSearchSort.getRealString(pageable.getSort().toString()),
        pageable.getPageNumber());
    List<BlogSearchResult> entries = new ArrayList<>();
    for (KakaoBlogSearchEntry entry : kakaoBlogSearchResult.getDocuments()) {
      entries.add(BlogSearchResult.builder()
          .title(entry.getTitle())
          .contents(entry.getContents())
          .dateTime(entry.getDateTime())
          .thumbnail(entry.getThumbnail())
          .url(entry.getUrl())
          .blogName(entry.getBlogName())
          .build());
    }

    return entries;
  }

}
