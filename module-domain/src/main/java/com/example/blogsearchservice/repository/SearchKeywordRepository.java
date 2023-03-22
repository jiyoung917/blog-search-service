package com.example.blogsearchservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.blogsearchservice.domain.SearchKeyword;

import jakarta.persistence.LockModeType;

@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {

  List<SearchKeyword> findTop10ByOrderBySearchCountDesc();

  @Lock(LockModeType.OPTIMISTIC)
  @Query("SELECT k from SearchKeyword k where k.keyword = :keyword")
  SearchKeyword findByWithOptimisticLock(final String keyword);

}