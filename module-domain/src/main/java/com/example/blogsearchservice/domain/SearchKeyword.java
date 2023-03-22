package com.example.blogsearchservice.domain;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "SearchKeyword",
    indexes = {
        @Index(name = "idx__keyword", columnList = "keyword"),
        @Index(name = "idx__search_count", columnList = "search_count") })
@Entity
public class SearchKeyword {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String keyword;

  @Column(name = "search_count")
  @ColumnDefault("1")
  private Long searchCount;

  @Version
  private Integer version;

  public SearchKeyword(String keyword) {
    this.keyword = keyword;
  }

  @PrePersist
  public void prePersist() {
    this.searchCount = this.searchCount == null ? 1 : this.searchCount;
  }

}
