package com.example.blogsearchservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients(basePackages = "com.example.blogsearchservice")
@EntityScan(basePackages = {"com.example.blogsearchservice"})
@EnableJpaRepositories(basePackages = {"com.example.blogsearchservice"})
@SpringBootApplication(scanBasePackages = "com.example.blogsearchservice")
public class AppApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(AppApiApplication.class, args);
  }

}
