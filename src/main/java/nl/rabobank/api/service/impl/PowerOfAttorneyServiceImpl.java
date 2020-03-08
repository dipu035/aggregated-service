package nl.rabobank.api.service.impl;

import static nl.rabobank.api.domain.CardType.CREDIT_CARD;
import static nl.rabobank.api.domain.CardType.DEBIT_CARD;
import static nl.rabobank.api.domain.Status.ACTIVE;
import static org.springframework.util.StringUtils.isEmpty;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import lombok.RequiredArgsConstructor;
import nl.rabobank.api.domain.Account;
import nl.rabobank.api.domain.CreditCard;
import nl.rabobank.api.domain.DebitCard;
import nl.rabobank.api.domain.PowerOfAttorney;
import nl.rabobank.api.domain.PowerOfAttorneyDetails;
import nl.rabobank.api.domain.Status;
import nl.rabobank.api.service.AccountService;
import nl.rabobank.api.service.CardService;
import nl.rabobank.api.service.PowerOfAttorneyService;
import nl.rabobank.api.service.RestClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class PowerOfAttorneyServiceImpl implements PowerOfAttorneyService {

  private static final String BANK_IDENTIFIER = "NL23RABO";

  private final RestClientService restClient;
  private final AccountService accountService;
  private final CardService cardService;

  @Value("${powerOfAttorenyUrl:http://localhost:8080/power-of-attorneys}")
  private String powerOfAttorenyUrl;


  @Override
  public PowerOfAttorneyDetails getPowerOfAttorneyDetails(String id) {
    URI uri = fromHttpUrl(powerOfAttorenyUrl).pathSegment(id).build().encode().toUri();
    PowerOfAttorney powerOfAttorney = restClient
        .exchange(uri, HttpMethod.GET, PowerOfAttorney.class);
    return createPowerOfAttorenyDetails(powerOfAttorney);
  }

  private PowerOfAttorneyDetails createPowerOfAttorenyDetails(PowerOfAttorney powerOfAttorney) {
    PowerOfAttorneyDetails powerOfAttorneyDetails = new PowerOfAttorneyDetails();
    powerOfAttorneyDetails.setId(powerOfAttorney.getId());
    powerOfAttorneyDetails.getAuthorizations().addAll(powerOfAttorney.getAuthorizations());
    powerOfAttorneyDetails.setGrantor(powerOfAttorney.getGrantor());
    powerOfAttorneyDetails.setGrantee(powerOfAttorney.getGrantee());
    powerOfAttorneyDetails.setDirection(powerOfAttorney.getDirection());
    powerOfAttorneyDetails.setAccountDetails(getAccountDetails(powerOfAttorney.getAccount()));
    setCardDetails(powerOfAttorney, powerOfAttorneyDetails);
    return powerOfAttorneyDetails;
  }

  private void setCardDetails(PowerOfAttorney powerOfAttorney,
      PowerOfAttorneyDetails powerOfAttorneyDetails) {
    powerOfAttorney.getCards().forEach(cardReference -> {
      if (DEBIT_CARD.equals(cardReference.getType())) {
        DebitCard debitCard = cardService.getDebitCardDetails(cardReference.getId());
        //Only show the active cards;
        if (isActive(debitCard.getStatus())) {
          powerOfAttorneyDetails.getDebitCards().add(debitCard);
        }
      }

      if (CREDIT_CARD.equals(cardReference.getType())) {
        CreditCard creditCard = cardService.getCreditCardDetails(cardReference.getId());
        //Only show the active cards;
        if (isActive(creditCard.getStatus())) {
          powerOfAttorneyDetails.getCreditCards().add(creditCard);
        }
      }
    });
  }

  private boolean isActive(Status status) {
    return ACTIVE.equals(status);
  }

  private Account getAccountDetails(String account) {
    if (isEmpty(account)) {
      return null;
    }
    String accountId = retrieveAccountId(account);
    return accountService.getAccountDetails(accountId);
  }

  private String retrieveAccountId(String account) {
    //account = BANK_IDENTIFIER + accountId.
    return account.replace(BANK_IDENTIFIER, "");
  }
}
