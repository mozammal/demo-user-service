package org.example.infrastructure.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.example.application.UsersUseCase;
import org.example.application.domain.port.UserRepository;

@ApplicationScoped
public class UserServiceConfig {

  @Produces
  public UsersUseCase usersUseCase(UserRepository repo) {
    return new UsersUseCase(repo);
  }
}
