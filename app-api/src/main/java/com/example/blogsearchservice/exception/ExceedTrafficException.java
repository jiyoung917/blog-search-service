package com.example.blogsearchservice.exception;

public class ExceedTrafficException extends RuntimeException {

  public ExceedTrafficException() {
    super("Traffic exceeded");
  }

}
