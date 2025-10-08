package org.example.util;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import org.example.application.UsersUseCase;
import org.example.application.domain.User;
import org.example.application.domain.port.UserRepository;
import org.jboss.logging.Logger;

@Alternative
@Priority(1)
@ApplicationScoped
public class UsersUseCaseRetry extends UsersUseCase {

  private static final Logger LOG = Logger.getLogger(UsersUseCaseRetry.class);

  private int attempts = 0;

  @Inject
  public UsersUseCaseRetry(UserRepository repo) {
    super(repo);
  }

  public UsersUseCaseRetry() {
    super(null);
  }

  @Override
  public Uni<User> createUser(User user) {
    attempts++;
    LOG.infof("Attempt #%d to create user: %s", attempts, user.name());

    if (attempts <= 2) {
      LOG.warnf("Simulated failure on attempt #%d for user %s", attempts, user.name());
      return Uni.createFrom().failure(new IllegalStateException("Simulated failure"));
    }

    LOG.infof("Success on attempt #%d for user %s", attempts, user.name());
    return super.createUser(user)
        .invoke(u -> LOG.infof("User persisted successfully: %s", u))
        .onFailure()
        .invoke(err -> LOG.errorf("Unexpected error on final attempt: %s", err.getMessage()));
  }

  public void resetAttempts() {
    this.attempts = 0;
  }

  public int getAttempts() {
    return attempts;
  }
}
