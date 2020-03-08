package nl.rabobank.api.service;

import nl.rabobank.api.domain.TokenRequest;

public interface AuthenticationService {
  String generateToken(TokenRequest tokenRequest);
}
