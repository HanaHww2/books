package com.book;

import com.book.common.support.DatabaseCleanUp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.utility.TestcontainersConfiguration;

@Import({DatabaseCleanUp.class, TestcontainersConfiguration.class})
@ActiveProfiles("test")
@SpringBootTest
class BookApplicationTests {

  @Test
  void contextLoads() {
  }

}
