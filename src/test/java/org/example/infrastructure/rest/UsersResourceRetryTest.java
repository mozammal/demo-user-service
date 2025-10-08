package org.example.infrastructure.rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.example.application.domain.UserDTO;
import org.example.util.PostgresResource;
import org.example.util.RetryTestProfile;
import org.example.util.UsersUseCaseRetry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(PostgresResource.class)
@TestProfile(RetryTestProfile.class)
class UsersResourceRetryTest {

  @Inject UsersResource resource;

  @Inject UsersUseCaseRetry testUseCase;

  @BeforeEach
  void setup() {
    testUseCase.resetAttempts();
  }

  @Test
  void testRetryOnFailure() {
    UserDTO dto = new UserDTO("Ana", "ana@example.com");
    String idempotencyKey = "retry-key";
    given()
        .header("Idempotency-Key", "key-1")
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/users")
        .then()
        .statusCode(201);

    assertEquals(3, testUseCase.getAttempts());
  }
}
