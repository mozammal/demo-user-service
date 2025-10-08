package org.example.application.domain;

import java.util.Objects;

public record User(Long id, String name, String email, String idempotencyKey) {
  public User {
    validateUserInput(name, email, idempotencyKey);
  }

  private void validateUserInput(String name, String email, String idempotencyKey) {
    validateNotBlank(name, "name");
    validateNotBlank(email, "email");
    validateNotBlank(idempotencyKey, "idempotencyKey");

    if (!isValidEmail(email)) {
      throw new IllegalArgumentException("Invalid email format");
    }
  }

  private void validateNotBlank(String value, String fieldName) {
    if (Objects.isNull(value) || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be null or empty");
    }
  }

  private boolean isValidEmail(String email) {
    return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
  }

  public static User fromDTO(UserDTO dto, String idempotencyKey) {
    return new User(null, dto.name(), dto.email(), idempotencyKey);
  }
}
