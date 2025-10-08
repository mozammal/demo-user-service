package org.example.infrastructure.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    int status = getStatusCode(exception);
    var error = new ErrorResponse(status, exception.getMessage());

    return Response.status(status).type(MediaType.APPLICATION_JSON).entity(error).build();
  }

  private int getStatusCode(Throwable e) {
    if (e instanceof IllegalArgumentException) {
      return Response.Status.BAD_REQUEST.getStatusCode();
    }
    if (e instanceof org.hibernate.exception.ConstraintViolationException) {
      return Response.Status.CONFLICT.getStatusCode();
    }
    if (e instanceof IllegalStateException) {
      return Response.Status.CONFLICT.getStatusCode();
    }

    return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
  }

  private static class ErrorResponse {
    public int status;
    public String message;

    public ErrorResponse(int status, String message) {
      this.status = status;
      this.message = message;
    }
  }
}
