package com.example.blogsearchservice.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum KakaoBlogSearchSort {

  ACCURACY("accu", "accuracy"),
  RECENCY("recency", "recency");

  private final String alias;
  private final String realString;

  public static String getRealString(String alias) {
    for (KakaoBlogSearchSort sort : values()) {
      if (alias.contains(sort.alias)) {
        return sort.realString;
      }
    }

    throw new IllegalArgumentException("Not supported sort type : " + alias);
  }

}
