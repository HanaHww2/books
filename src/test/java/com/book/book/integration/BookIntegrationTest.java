package com.book.book.integration;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.book.book.domain.entity.Book;
import com.book.book.domain.repository.BookRepository;
import com.book.book.fixture.BookFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.utility.TestcontainersConfiguration;

@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class BookIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private BookRepository bookRepository;

  private Book book;

  @BeforeEach
  void setUp() {
    book = BookFixture.create();
    bookRepository.save(book);
  }

  @DisplayName("도서 상세 정보 조회 api - 200 성공")
  @Test
  void getBookDetailInfo() throws Exception {

    // given
    DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("isbn", "1234567890123");

    // when
    ResultActions resultActions = mockMvc.perform(
        get("/api/v1/books")
            .params(params));

    // then
    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(book.getId()))
        .andExpect(jsonPath("$.isbn").value(book.getIsbn()))
        .andExpect(jsonPath("$.title").value(book.getBaseInfo().getTitle()))
        .andExpect(jsonPath("$.subTitle").value(book.getBaseInfo().getSubTitle()))
        .andExpect(jsonPath("$.author").value(book.getBaseInfo().getAuthor()))
        .andExpect(jsonPath("$.description").value(book.getExtraInfo().getDescription()))
        .andExpect(jsonPath("$.price").value(book.getExtraInfo().getPrice()))
        .andExpect(jsonPath("$.image").value(book.getExtraInfo().getImage()))
        .andExpect(jsonPath("$.publisher").value(book.getPublishInfo().getPublisher()))
        .andExpect(jsonPath("$.published").value(book.getPublishInfo().getPublished().toString()))
        .andExpect(jsonPath("$.createdAt").value(ISO.format(book.getCreatedAt())))
        .andExpect(jsonPath("$.updatedAt").value(ISO.format(book.getUpdatedAt())))
        .andExpect(jsonPath("$.deletedAt").value(nullValue()));
  }
}
