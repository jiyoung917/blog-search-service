package com.example.blogsearchservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.example.blogsearchservice.domain.SearchKeyword;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {

  List<SearchKeyword> findTop10ByOrderBySearchCountDesc();

  @Modifying
  @Query("UPDATE SearchKeyword k SET k.searchCount = k.searchCount + 1 WHERE k.keyword = :keyword")
  int updateSearchCount(final String keyword);

  @Lock(value = LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT k from SearchKeyword k where k.keyword = :keyword")
  @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "1000")})
  SearchKeyword findByWithPessimisticLock(final String keyword);

}