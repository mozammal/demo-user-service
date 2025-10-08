package org.example.application;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import jakarta.persistence.PersistenceException;
import org.example.application.domain.User;
import org.example.application.domain.port.UserRepository;
import org.example.faker.InMemoryUsersRepository;
import org.junit.jupiter.api.Test;

class UsersUseCaseTest {

  @Test
  void testSaveUser() {
    UserRepository repo = new InMemoryUsersRepository();
    UsersUseCase service = new UsersUseCase(repo);
    User u = new User(1L, "Anna", "Anna@example.com", "key-1");

    repo.save(u).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();

    service
        .getUser(1L)
        .subscribe()
        .withSubscriber(UniAssertSubscriber.create())
        .assertCompleted()
        .assertItem(u);
  }

  @Test
  void testGetUser() {
    UserRepository repo = new InMemoryUsersRepository();
    UsersUseCase service = new UsersUseCase(repo);
    User u = new User(1L, "Alice", "alice@example.com", "xx");

    repo.save(u).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();

    service
        .getUser(1L)
        .subscribe()
        .withSubscriber(UniAssertSubscriber.create())
        .assertCompleted()
        .assertItem(u);
  }

  @Test
  void testDuplicateEmailWithDifferentIdempotencyKeyThrowsConstraintViolation() {
    UserRepository repo = new InMemoryUsersRepository();
    User u1 = new User(1L, "Alice1", "alice@example.com", "xx");
    User u2 = new User(2L, "Alice2", "alice@example.com", "yy");

    repo.save(u1).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();

    repo.save(u2)
        .subscribe()
        .withSubscriber(UniAssertSubscriber.create())
        .assertFailedWith(
            PersistenceException.class,
            "Unique constraint violation: email already exists with a different idempotencyKey");
  }
}
