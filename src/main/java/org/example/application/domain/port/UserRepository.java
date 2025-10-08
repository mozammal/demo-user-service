package org.example.application.domain.port;

import io.smallrye.mutiny.Uni;
import org.example.application.domain.User;

public interface UserRepository {
  Uni<User> save(User user);

  Uni<User> findById(Long id);

  Uni<User> findByIdempotencyKey(String key);
}
