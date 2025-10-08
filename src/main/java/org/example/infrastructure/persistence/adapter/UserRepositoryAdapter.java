package org.example.infrastructure.persistence.adapter;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Objects;
import org.example.application.domain.User;
import org.example.application.domain.port.UserRepository;

@ApplicationScoped
public class UserRepositoryAdapter implements UserRepository {

  @Override
  public Uni<User> save(User user) {
    UserEntity entity = UserEntity.fromDomain(user);
    return entity.<UserEntity>persist().map(UserEntity::toDomain);
  }

  @Override
  public Uni<User> findByIdempotencyKey(String idempotencyKey) {
    return UserEntity.<UserEntity>find("idempotencyKey", idempotencyKey)
        .firstResult()
        .map(user -> Objects.isNull(user) ? null : user.toDomain());
  }

  @Override
  public Uni<User> findById(Long id) {
    return UserEntity.<UserEntity>find("id", id)
        .firstResult()
        .map(user -> Objects.isNull(user) ? null : user.toDomain());
  }
}
