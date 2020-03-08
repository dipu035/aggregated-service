package nl.rabobank.api.service.impl;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import lombok.RequiredArgsConstructor;
import nl.rabobank.api.domain.Account;
import nl.rabobank.api.service.AccountService;
import nl.rabobank.api.service.RestClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.net.URI;
import javax.cache.annotation.CacheResult;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final RestClientService restClient;

  @Value("${powerOfAttorenyUrl:http://localhost:8080/accounts}")
  private String accountUrl;

  @Override
  @CacheResult(cacheName = "accountDetails")
  public Account getAccountDetails(String id) {
    URI uri = fromHttpUrl(accountUrl).pathSegment(id).build().encode().toUri();
    return restClient.exchange(uri, HttpMethod.GET, Account.class);
  }
}
