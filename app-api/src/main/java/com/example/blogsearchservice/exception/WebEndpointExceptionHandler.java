package com.example.blogsearchservice.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.google.common.collect.ImmutableMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class WebEndpointExceptionHandler {

  @ExceptionHandler({
      IllegalArgumentException.class,
  })
  public ResponseEntity<Map<String, String>> handleRegionNotFoundException(RuntimeException ex) {
    log.warn(ex.getMessage());
    Map<String, String> errorAttrs = ImmutableMap.of("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorAttrs);
  }

}

