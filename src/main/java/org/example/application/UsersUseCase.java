package org.example.application;

import io.smallrye.mutiny.Uni;
import org.example.application.domain.User;
import org.example.application.domain.port.UserRepository;

public class UsersUseCase {

  private final UserRepository userRepository;

  public UsersUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Uni<User> createUser(User user) {
    return userRepository
        .findByIdempotencyKey(user.idempotencyKey())
        .onItem()
        .ifNotNull()
        .transformToUni(existingUser -> Uni.createFrom().item(existingUser))
        .onItem()
        .ifNull()
        .switchTo(() -> userRepository.save(user));
  }

  public Uni<User> getUser(Long id) {
    return userRepository.findById(id);
  }
}
