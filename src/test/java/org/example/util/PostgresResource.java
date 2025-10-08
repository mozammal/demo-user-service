package org.example.util;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.Map;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresResource implements QuarkusTestResourceLifecycleManager {

  private PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:16-alpine")
          .withDatabaseName("testdb")
          .withUsername("test")
          .withPassword("test");

  @Override
  public Map<String, String> start() {
    postgres.start();

    return Map.of(
        "quarkus.datasource.db-kind",
        "postgresql",
        "quarkus.datasource.reactive.url",
        "postgresql://" + postgres.getHost() + ":" + postgres.getFirstMappedPort() + "/testdb",
        "quarkus.datasource.username",
        postgres.getUsername(),
        "quarkus.datasource.password",
        postgres.getPassword(),
        "quarkus.hibernate-orm.database.generation",
        "drop-and-create");
  }

  @Override
  public void stop() {
    postgres.stop();
  }
}
