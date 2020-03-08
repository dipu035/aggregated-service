package nl.rabobank.api.service;

import org.springframework.http.HttpMethod;

import java.net.URI;

public interface RestClientService {

  /**
   * Executes the corresponding RestTemplate.exchange() method.
   */
  <V> V exchange(URI uri, HttpMethod method, Class<V> responseType);
}
