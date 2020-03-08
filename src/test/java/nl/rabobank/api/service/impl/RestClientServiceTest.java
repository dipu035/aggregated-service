package nl.rabobank.api.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import nl.rabobank.api.exception.GenericException;
import nl.rabobank.api.exception.NotFoundException;
import nl.rabobank.api.exception.ValidationException;
import nl.rabobank.api.service.RestClientService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RunWith(MockitoJUnitRunner.class)
public class RestClientServiceTest {

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private HttpStatusCodeException exception;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private RestClientService service;

  @Before
  public void setUp() {
    service = new RestClientServiceImpl(restTemplate);
  }

  @Test
  public void testExchange_ok() {
    //arrange
    String responseMessage = "Success";
    URI uri = URI.create("uri");
    ResponseEntity<String> responseEntity = ResponseEntity.ok(responseMessage);
    when(restTemplate.exchange(any(), any(), any(), any(Class.class))).thenReturn(responseEntity);

    //act
    String response = service.exchange(uri, HttpMethod.GET, String.class);

    //assert
    assertThat(response, equalTo(responseMessage));
  }

  @Test
  public void testExchange_not_found() {
    //arrange
    String notFound = "Not found";
    URI uri = URI.create("uri");
    when(restTemplate.exchange(any(), any(), any(), any(Class.class)))
        .thenThrow(exception);
    when(exception.getStatusCode()).thenReturn(NOT_FOUND);

    //act & assert
    thrown.expect(NotFoundException.class);
    thrown.expectMessage("Requested resource is not found.");
    service.exchange(uri, HttpMethod.GET, String.class);
  }

  @Test
  public void testExchange_bad_request() {
    //arrange
    String badRequest = "Bad Request";
    URI uri = URI.create("uri");
    when(restTemplate.exchange(any(), any(), any(), any(Class.class)))
        .thenThrow(exception);
    when(exception.getStatusCode()).thenReturn(BAD_REQUEST);
    when(exception.getMessage()).thenReturn(badRequest);

    //act & assert
    thrown.expect(ValidationException.class);
    thrown.expectMessage(badRequest);
    service.exchange(uri, HttpMethod.GET, String.class);
  }

  @Test
  public void testExchange_server_error() {
    //arrange
    String internalServerError = "Internal server error";
    URI uri = URI.create("uri");
    when(restTemplate.exchange(any(), any(), any(), any(Class.class)))
        .thenThrow(exception);
    when(exception.getStatusCode()).thenReturn(INTERNAL_SERVER_ERROR);
    when(exception.getMessage()).thenReturn(internalServerError);

    //act & assert
    thrown.expect(GenericException.class);
    thrown.expectMessage(internalServerError);
    service.exchange(uri, HttpMethod.GET, String.class);
  }
}
