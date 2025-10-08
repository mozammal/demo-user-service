package org.example.infrastructure.persistence.adapter;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.Objects;
import org.example.application.domain.User;

@Entity(name = "users")
public class UserEntity extends PanacheEntity {

  @Column(nullable = false)
  public String name;

  @Column(nullable = false, unique = true)
  public String email;

  @Column(nullable = false, unique = true)
  public String idempotencyKey;

  public static UserEntity fromDomain(User user) {
    Objects.requireNonNull(user, "User cannot be null");

    UserEntity userEntity = new UserEntity();
    userEntity.id = user.id();
    userEntity.name = user.name();
    userEntity.email = user.email();
    userEntity.idempotencyKey = user.idempotencyKey();
    return userEntity;
  }

  public User toDomain() {
    return new User(id, name, email, idempotencyKey);
  }
}
