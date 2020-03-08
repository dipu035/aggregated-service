package nl.rabobank.api.controller;

import lombok.RequiredArgsConstructor;
import nl.rabobank.api.domain.PowerOfAttorneyDetails;
import nl.rabobank.api.service.PowerOfAttorneyService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = "/api/admin/power-of-attorneys",
    produces = {"application/json"})
@RequiredArgsConstructor
@Validated
public class PowerOfAttorneyController {

  private final PowerOfAttorneyService service;

  @GetMapping(path = "/{id}")
  public ResponseEntity<PowerOfAttorneyDetails> getPowerOfAttorney(@PathVariable String id) {
    return ResponseEntity.ok(service.getPowerOfAttorneyDetails(id));
  }
}
