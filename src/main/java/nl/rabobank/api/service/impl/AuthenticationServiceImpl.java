package nl.rabobank.api.service.impl;

import lombok.RequiredArgsConstructor;
import nl.rabobank.api.config.JwtTokenProvider;
import nl.rabobank.api.domain.TokenRequest;
import nl.rabobank.api.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager authenticationManager;

  @Value("${user.role:ADMIN}")
  private String role;

  @Override
  public String generateToken(TokenRequest tokenRequest) {
    authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(tokenRequest.getUsername(),
            tokenRequest.getPassword()));
    String token= jwtTokenProvider
        .createToken(tokenRequest.getUsername(), role);
    return token;
  }
}
