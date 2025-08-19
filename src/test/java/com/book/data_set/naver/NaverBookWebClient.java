package com.book.data_set.naver;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Profile("test")
@Component
public class NaverBookWebClient {

  private static final String BASE_URL = "https://openapi.naver.com";

  private final WebClient webClient;

  private final String naverClientId;

  private final String naverClientSecret;

  public NaverBookWebClient(
      @Value("${NAVER_CLIENT_ID:dummy}") String clientId,
      @Value("${NAVER_CLIENT_SECRET:dummy}") String clientSecret
  ) {
    if (Objects.isNull(clientId) || Objects.isNull(clientSecret)) {
      throw new IllegalStateException("NAVER_CLIENT_ID, NAVER_CLIENT_SECRET 환경 변수가 필요합니다.");
    }
    this.naverClientId = clientId;
    this.naverClientSecret = clientSecret;

    this.webClient = WebClient.builder()
        .baseUrl(BASE_URL)
        .defaultHeader("X-Naver-Client-Id", clientId)
        .defaultHeader("X-Naver-Client-Secret", clientSecret)
        .build();
  }

  public NaverBookResponse search(String query, int display, int start) {
    return webClient.get()
        .uri(b -> b.path("/v1/search/book.json")
            .queryParam("query", query)
            .queryParam("display", display)
            .queryParam("start", start)
            .build())
        .retrieve()
        .bodyToMono(NaverBookResponse.class)
        .onErrorResume(ex -> Mono.empty())
        .block();
  }
}
