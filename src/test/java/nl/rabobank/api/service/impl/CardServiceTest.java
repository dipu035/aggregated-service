package nl.rabobank.api.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import nl.rabobank.api.domain.CreditCard;
import nl.rabobank.api.domain.DebitCard;
import nl.rabobank.api.domain.Status;
import nl.rabobank.api.service.CardService;
import nl.rabobank.api.service.RestClientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceTest {

  private static final String CARD_HOLDER = "Super duper company";
  private static final String ID = "100";
  private static final BigDecimal BALANCE = new BigDecimal(100);

  @Mock
  private RestClientService restClient;

  private CardService service;

  @Before
  public void setUp() {
    service = new CardServiceImpl(restClient);
    ReflectionTestUtils.setField(service, "debitCardUrl", "http://www.google.com/debit");
    ReflectionTestUtils.setField(service, "creditCardUrl", "http://www.google.com/credit");
  }

  @Test
  public void testGetDebitCardDetails() {
    //arrange
    when(restClient.exchange(any(), any(), any(Class.class))).thenReturn(createDebitCard());

    //act
    DebitCard debitCard = service.getDebitCardDetails(ID);

    //assert
    assertThat(debitCard.getCardHolder(), equalTo(CARD_HOLDER));
    assertThat(debitCard.getId(), equalTo(ID));
  }

  @Test
  public void testGetCreditCardDetails() {
    //arrange
    when(restClient.exchange(any(), any(), any(Class.class))).thenReturn(createCreditCard());

    //act
    CreditCard creditCard = service.getCreditCardDetails(ID);

    //assert
    assertThat(creditCard.getCardHolder(), equalTo(CARD_HOLDER));
    assertThat(creditCard.getId(), equalTo(ID));
  }

  private CreditCard createCreditCard() {
    CreditCard creditCard = new CreditCard();
    creditCard.setCardHolder(CARD_HOLDER);
    creditCard.setId(ID);
    creditCard.setStatus(Status.ACTIVE);
    return creditCard;
  }

  private DebitCard createDebitCard() {
    DebitCard debitCard = new DebitCard();
    debitCard.setCardHolder(CARD_HOLDER);
    debitCard.setId(ID);
    debitCard.setStatus(Status.ACTIVE);
    return debitCard;
  }
}
