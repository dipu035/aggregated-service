package nl.rabobank.api.service.impl;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import lombok.RequiredArgsConstructor;
import nl.rabobank.api.domain.CreditCard;
import nl.rabobank.api.domain.DebitCard;
import nl.rabobank.api.service.CardService;
import nl.rabobank.api.service.RestClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.net.URI;
import javax.cache.annotation.CacheResult;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

  private final RestClientService restClient;

  @Value("${debitCardUrl:http://localhost:8080/debit-cards}")
  private String debitCardUrl;

  @Value("${creditCardUrl:http://localhost:8080/credit-cards}")
  private String creditCardUrl;

  @Override
  @CacheResult(cacheName = "debitCardDetails")
  public DebitCard getDebitCardDetails(String id) {
    URI uri = fromHttpUrl(debitCardUrl).pathSegment(id).build().encode().toUri();
    return getCardDetails(uri, DebitCard.class);
  }

  @Override
  @CacheResult(cacheName = "creditCardDetails")
  public CreditCard getCreditCardDetails(String id) {
    URI uri = fromHttpUrl(creditCardUrl).pathSegment(id).build().encode().toUri();
    return getCardDetails(uri, CreditCard.class);
  }

  private <T> T getCardDetails(URI uri, Class<T> responseType) {
    return restClient.exchange(uri, HttpMethod.GET, responseType);
  }
}
