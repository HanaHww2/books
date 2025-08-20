package com.book;

import com.book.common.container.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestBookApplication {

  public static void main(String[] args) {
    SpringApplication.from(BookApplication::main).with(TestcontainersConfiguration.class).run(args);
  }

}
