package nl.rabobank.api.service.impl;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.RequiredArgsConstructor;
import nl.rabobank.api.exception.GenericException;
import nl.rabobank.api.exception.NotFoundException;
import nl.rabobank.api.exception.ValidationException;
import nl.rabobank.api.service.RestClientService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class RestClientServiceImpl implements RestClientService {

  private final RestTemplate restTemplate;

  @Override
  public <V> V exchange(URI uri, HttpMethod method, Class<V> responseType) {
    HttpEntity<String> httpEntity = createEntity();
    return processResponse(() -> restTemplate.exchange(uri, method, httpEntity, responseType));
  }

  private HttpEntity<String> createEntity() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(headers);
  }

  private <V> V processResponse(Supplier<ResponseEntity<V>> restCall) {
    try {
      ResponseEntity<V> response = restCall.get();
      return Objects.requireNonNull(response.getBody());
    } catch (HttpStatusCodeException e) {
      if (NOT_FOUND.equals(e.getStatusCode())) {
        String message = "Requested resource is not found.";
        throw new NotFoundException(message);
      }
      if (BAD_REQUEST.equals(e.getStatusCode())) {
        throw new ValidationException(e.getMessage());
      }
      throw new GenericException(e.getMessage());
    }
  }

}
