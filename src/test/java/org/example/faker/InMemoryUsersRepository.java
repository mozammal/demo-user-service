package org.example.faker;

import io.smallrye.mutiny.Uni;
import jakarta.persistence.PersistenceException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.example.application.domain.User;
import org.example.application.domain.port.UserRepository;

public class InMemoryUsersRepository implements UserRepository {

  private final Map<Long, User> storage = new ConcurrentHashMap<>();

  @Override
  public Uni<User> save(User user) {
    boolean emailExistsWithDifferentIdempotencyKey =
        storage.values().stream()
            .anyMatch(
                existing ->
                    existing.email().equals(user.email())
                        && !existing.idempotencyKey().equals(user.idempotencyKey()));

    if (emailExistsWithDifferentIdempotencyKey) {
      return Uni.createFrom()
          .failure(
              new PersistenceException(
                  "Unique constraint violation: email already exists with a different idempotencyKey"));
    }

    storage.put(user.id(), user);
    return Uni.createFrom().item(user);
  }

  @Override
  public Uni<User> findById(Long id) {
    return Uni.createFrom().item(storage.get(id));
  }

  @Override
  public Uni<User> findByIdempotencyKey(String key) {
    return Uni.createFrom()
        .item(
            storage.values().stream()
                .filter(u -> u.idempotencyKey().equals(key))
                .findFirst()
                .orElse(null));
  }
}
