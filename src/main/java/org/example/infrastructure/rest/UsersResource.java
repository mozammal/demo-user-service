package org.example.infrastructure.rest;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.example.application.UsersUseCase;
import org.example.application.domain.User;
import org.example.application.domain.UserDTO;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

  private final UsersUseCase userService;

  public UsersResource(UsersUseCase userService) {
    this.userService = userService;
  }

  @Consumes(MediaType.APPLICATION_JSON)
  @POST
  @WithTransaction
  @Retry(maxRetries = 5, delay = 200, jitter = 50)
  public Uni<Response> createUser(
      UserDTO user, @HeaderParam("Idempotency-Key") String idempotencyKey) {

    if (idempotencyKey == null || idempotencyKey.isBlank()) {
      return Uni.createFrom()
          .item(
              Response.status(Response.Status.BAD_REQUEST)
                  .entity("Missing Idempotency-Key header")
                  .build());
    }

    return userService
        .createUser(User.fromDTO(user, idempotencyKey))
        .onFailure(IllegalStateException.class)
        .retry()
        .withBackOff(java.time.Duration.ofMillis(200), java.time.Duration.ofMillis(250))
        .atMost(5)
        .onItem()
        .transform(savedUser -> Response.status(Response.Status.CREATED).entity(savedUser).build())
        .onFailure(IllegalArgumentException.class)
        .recoverWithItem(
            err -> Response.status(Response.Status.BAD_REQUEST).entity(err.getMessage()).build());
  }

  @WithTransaction
  @GET
  @Path("/{id}")
  public Uni<Response> getUser(@PathParam("id") Long id) {
    return userService
        .getUser(id)
        .onItem()
        .ifNotNull()
        .transform(user -> Response.ok(user).build())
        .onItem()
        .ifNull()
        .continueWith(Response.status(Response.Status.NOT_FOUND).entity("User not found").build());
  }
}
