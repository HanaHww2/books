package com.book.common.support;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
@RequiredArgsConstructor
public class DatabaseCleanUp {

  @PersistenceContext
  private final EntityManager entityManager;
  private List<String> tableNames;

  @PostConstruct
  public void afterPropertiesSet() {
    tableNames = entityManager.getMetamodel()
        .getEntities()
        .stream()
        .filter(entityType -> entityType
            .getJavaType()
            .getAnnotation(Entity.class) != null)
        .map(entityType -> {
          Table table = entityType
              .getJavaType()
              .getAnnotation(Table.class);

          return Objects.nonNull(table) && Objects.nonNull(table.name()) ?
              table.name() :
              convertToLowerUnderscore(entityType.getName());
        })
        .collect(Collectors.toList());
  }

  @Transactional
  public void execute() {
    // 쓰기 지연 저장소에 남은 SQL을 마저 수행
    entityManager.flush();

    if (tableNames.isEmpty()) return;

    String joined = String.join(", ", tableNames);
    // FK 의존성까지 함께 비우고, identity/sequence를 초기화
    String sql = "TRUNCATE TABLE " + joined + " RESTART IDENTITY CASCADE";
    entityManager.createNativeQuery(sql).executeUpdate();
  }

  private String convertToLowerUnderscore(String camelCase) {
    return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
  }
}
