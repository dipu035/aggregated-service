package nl.rabobank.api.service;

import nl.rabobank.api.domain.CreditCard;
import nl.rabobank.api.domain.DebitCard;

public interface CardService {

  DebitCard getDebitCardDetails(String id);

  CreditCard getCreditCardDetails(String id);
}
