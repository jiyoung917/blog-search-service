package com.example.blogsearchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HotKeywordInfo {

  private String keyword;

  private Long searchCount;

}
