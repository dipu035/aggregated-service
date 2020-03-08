package nl.rabobank.api.exception.handler;


import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import lombok.extern.slf4j.Slf4j;
import nl.rabobank.api.exception.GenericException;
import nl.rabobank.api.exception.NotFoundException;
import nl.rabobank.api.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler({NotFoundException.class})
  public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
    return constructResponseEntity(NOT_FOUND, e);
  }

  @ExceptionHandler({ValidationException.class})
  public ResponseEntity<String> handleValidationException(ValidationException e) {
    return constructResponseEntity(BAD_REQUEST, e);
  }

  @ExceptionHandler({GenericException.class})
  public ResponseEntity<String> handleGenericException(GenericException e) {
    return constructResponseEntity(INTERNAL_SERVER_ERROR, e);
  }

  @ExceptionHandler({AuthenticationException.class})
  public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
    return constructResponseEntity(UNPROCESSABLE_ENTITY, e);
  }

  private ResponseEntity<String> constructResponseEntity(HttpStatus status, Exception e) {
    log.error("Exception : ", e);
    return ResponseEntity.status(status).body(e.getMessage());
  }

}
