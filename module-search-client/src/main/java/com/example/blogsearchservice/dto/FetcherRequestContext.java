package com.example.blogsearchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FetcherRequestContext {

  private String query;

  private String sort;

  private int pageNumber;

  private int pageSize;

}
