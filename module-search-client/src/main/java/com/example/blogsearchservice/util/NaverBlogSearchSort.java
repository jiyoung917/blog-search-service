package com.example.blogsearchservice.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NaverBlogSearchSort {

  ACCU("accu", "sim"),
  RECENCY("recency", "date");

  private final String alias;
  private final String realString;

  public static String getRealString(String alias) {
    for (NaverBlogSearchSort sort : values()) {
      if (alias.contains(sort.alias)) {
        return sort.realString;
      }
    }

    throw new IllegalArgumentException("Not supported sort type : " + alias);
  }

}
