package nl.rabobank.api.exception.handler;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import nl.rabobank.api.exception.GenericException;
import nl.rabobank.api.exception.NotFoundException;
import nl.rabobank.api.exception.ValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

@RunWith(MockitoJUnitRunner.class)
public class ControllerAdviceTest {

  private ControllerAdvice advice = new ControllerAdvice();


  @Test
  public void testHandleNotFoundException() {
    //arrange
    String message = "poa not found.";
    //act
    ResponseEntity<String> responseEntity = advice
        .handleNotFoundException(new NotFoundException(message));

    //assert
    assertThat(responseEntity.getStatusCode(), equalTo(NOT_FOUND));
    assertThat(responseEntity.getBody(), equalTo(message));
  }

  @Test
  public void testHandleValidationException() {
    //arrange
    String message = "invalid request.";
    //act
    ResponseEntity<String> responseEntity = advice
        .handleValidationException(new ValidationException(message));

    //assert
    assertThat(responseEntity.getStatusCode(), equalTo(BAD_REQUEST));
    assertThat(responseEntity.getBody(), equalTo(message));
  }

  @Test
  public void testHandleGenericException() {
    //arrange
    String message = "server error.";
    //act
    ResponseEntity<String> responseEntity = advice
        .handleGenericException(new GenericException(message));

    //assert
    assertThat(responseEntity.getStatusCode(), equalTo(INTERNAL_SERVER_ERROR));
    assertThat(responseEntity.getBody(), equalTo(message));
  }

  @Test
  public void testHandleAuthenticationException() {
    //arrange
    String message = "Bad credentials";
    //act
    ResponseEntity<String> responseEntity = advice
        .handleAuthenticationException(new BadCredentialsException(message));

    //assert
    assertThat(responseEntity.getStatusCode(), equalTo(UNPROCESSABLE_ENTITY));
    assertThat(responseEntity.getBody(), equalTo(message));
  }
}
