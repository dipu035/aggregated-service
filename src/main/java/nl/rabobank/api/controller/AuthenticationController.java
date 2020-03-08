package nl.rabobank.api.controller;

import lombok.RequiredArgsConstructor;
import nl.rabobank.api.domain.TokenRequest;
import nl.rabobank.api.domain.TokenResponse;
import nl.rabobank.api.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = "/api",
    produces = {"application/json"})
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping(value = "/generate-token")
  public ResponseEntity<TokenResponse> register(@RequestBody TokenRequest request) {
    return ResponseEntity.ok(new TokenResponse(service.generateToken(request)));
  }

}
