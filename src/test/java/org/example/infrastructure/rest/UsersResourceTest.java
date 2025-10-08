package org.example.infrastructure.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import org.example.application.domain.UserDTO;
import org.example.util.PostgresResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(PostgresResource.class)
class UsersResourceTest {

  @Inject PgPool client;

  @BeforeEach
  void cleanDb() {
    client.query("TRUNCATE TABLE users RESTART IDENTITY CASCADE").execute().await().indefinitely();
  }

  @Test
  void testCreateUserSuccessfully() {
    UserDTO dto = new UserDTO("Ana", "ana@example.com");
    String idempotencyKey = "key-xxx";

    given()
        .header("Idempotency-Key", idempotencyKey)
        .contentType(ContentType.JSON)
        .body(dto)
        .when()
        .post("/users")
        .then()
        .statusCode(201)
        .body("name", equalTo("Ana"))
        .body("email", equalTo("ana@example.com"));
  }

  @Test
  void testDuplicateEmailThrowsConflict() {
    UserDTO dto1 = new UserDTO("Adam", "adam@example.com");
    UserDTO dto2 = new UserDTO("Adam Again", "adam@example.com");

    given()
        .header("Idempotency-Key", "key-1")
        .contentType(ContentType.JSON)
        .body(dto1)
        .when()
        .post("/users")
        .then()
        .statusCode(201)
        .body("name", equalTo("Adam"))
        .body("email", equalTo("adam@example.com"));

    given()
        .header("Idempotency-Key", "key-2")
        .contentType(ContentType.JSON)
        .body(dto2)
        .when()
        .post("/users")
        .then()
        .statusCode(409);
  }

  @Test
  void testGetUserNotFound() {
    given().when().get("/users/999").then().statusCode(404).body(equalTo("User not found"));
  }
}
