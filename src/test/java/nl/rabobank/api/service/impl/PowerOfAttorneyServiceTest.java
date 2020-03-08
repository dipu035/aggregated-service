package nl.rabobank.api.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import nl.rabobank.api.domain.Account;
import nl.rabobank.api.domain.Authorization;
import nl.rabobank.api.domain.CardReference;
import nl.rabobank.api.domain.CardType;
import nl.rabobank.api.domain.CreditCard;
import nl.rabobank.api.domain.DebitCard;
import nl.rabobank.api.domain.Direction;
import nl.rabobank.api.domain.PowerOfAttorney;
import nl.rabobank.api.domain.PowerOfAttorneyDetails;
import nl.rabobank.api.domain.Status;
import nl.rabobank.api.service.AccountService;
import nl.rabobank.api.service.CardService;
import nl.rabobank.api.service.PowerOfAttorneyService;
import nl.rabobank.api.service.RestClientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PowerOfAttorneyServiceTest {

  private static final String POA_ID = "1";
  private static final String GRANTOR = "Super duper company";
  private static final BigDecimal BALANCE = new BigDecimal(100);
  private static final Direction DIRECTION = Direction.GIVEN;
  private static final Authorization AUTHORIZATION = Authorization.VIEW;
  private static final String DEBIT_CARD_ID = "1111";
  private static final String DEBIT_CARD_HOLDER = "Frodo Basggins";
  private static final String CREDIT_CARD_ID = "2222";
  private static final String CREDIT_CARD_HOLDER = "Boromir";
  private static final String ACCOUNT = "NL23RABO123456789";

  @Mock
  private RestClientService restClient;
  @Mock
  private AccountService accountService;
  @Mock
  private CardService cardService;

  private PowerOfAttorneyService service;

  @Before
  public void setUp() {
    service = new PowerOfAttorneyServiceImpl(restClient, accountService, cardService);
    ReflectionTestUtils.setField(service, "powerOfAttorenyUrl", "http://www.google.com/poa");
  }

  @Test
  public void testGetPowerOfAttorneyDetails_AllInfoPresent() {
    //arrange
    when(restClient.exchange(any(), any(), any(Class.class))).thenReturn(createPowerOfAttorney());
    when(accountService.getAccountDetails(anyString())).thenReturn(createAccountDetails());
    when(cardService.getDebitCardDetails(anyString())).thenReturn(createDebitCard());
    when(cardService.getCreditCardDetails(anyString())).thenReturn(createCreditCard());

    //act
    PowerOfAttorneyDetails powerOfAttorneyDetails = service.getPowerOfAttorneyDetails(POA_ID);

    //assert
    assertThat(powerOfAttorneyDetails.getGrantor(), equalTo(GRANTOR));
    assertThat(powerOfAttorneyDetails.getAuthorizations().size(), equalTo(1));
    assertThat(powerOfAttorneyDetails.getCreditCards().size(), equalTo(1));
    assertThat(powerOfAttorneyDetails.getDebitCards().size(), equalTo(1));
    assertThat(powerOfAttorneyDetails.getDirection(), equalTo(Direction.GIVEN));
    assertThat(powerOfAttorneyDetails.getAccountDetails().getBalance(), equalTo(BALANCE));
    assertThat(powerOfAttorneyDetails.getAccountDetails().getOwner(), equalTo(GRANTOR));
  }

  @Test
  public void testGetPowerOfAttorneyDetails_account_null() {
    //arrange
    PowerOfAttorney powerOfAttorney = createPowerOfAttorney();
    powerOfAttorney.setAccount(null);

    when(restClient.exchange(any(), any(), any(Class.class))).thenReturn(powerOfAttorney);
    when(cardService.getDebitCardDetails(anyString())).thenReturn(createDebitCard());
    when(cardService.getCreditCardDetails(anyString())).thenReturn(createCreditCard());

    //act
    PowerOfAttorneyDetails powerOfAttorneyDetails = service.getPowerOfAttorneyDetails(POA_ID);

    //assert
    assertNull(powerOfAttorneyDetails.getAccountDetails());
  }

  private CreditCard createCreditCard() {
    CreditCard creditCard = new CreditCard();
    creditCard.setCardHolder(CREDIT_CARD_HOLDER);
    creditCard.setId(CREDIT_CARD_ID);
    creditCard.setStatus(Status.ACTIVE);
    return creditCard;
  }

  private DebitCard createDebitCard() {
    DebitCard debitCard = new DebitCard();
    debitCard.setCardHolder(DEBIT_CARD_HOLDER);
    debitCard.setId(DEBIT_CARD_ID);
    debitCard.setStatus(Status.ACTIVE);
    return debitCard;
  }

  private Account createAccountDetails() {
    Account account = new Account();
    account.setBalance(BALANCE);
    account.setOwner(GRANTOR);
    return account;
  }

  private PowerOfAttorney createPowerOfAttorney() {
    PowerOfAttorney powerOfAttorney = new PowerOfAttorney();
    powerOfAttorney.setId(POA_ID);
    powerOfAttorney.setAccount(ACCOUNT);
    powerOfAttorney.setAuthorizations(List.of(AUTHORIZATION));
    powerOfAttorney.setCards(List.of(new CardReference(DEBIT_CARD_ID, CardType.DEBIT_CARD),
        new CardReference(CREDIT_CARD_ID, CardType.CREDIT_CARD)));
    powerOfAttorney.setDirection(DIRECTION);
    powerOfAttorney.setGrantor(GRANTOR);
    return powerOfAttorney;
  }

}
