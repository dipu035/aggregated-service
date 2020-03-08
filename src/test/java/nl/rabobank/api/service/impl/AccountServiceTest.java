package nl.rabobank.api.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import nl.rabobank.api.domain.Account;
import nl.rabobank.api.service.AccountService;
import nl.rabobank.api.service.RestClientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

  private static final String ACCOUNT_OWNER = "Super duper company";
  private static final BigDecimal BALANCE = new BigDecimal(100);

  @Mock
  private RestClientService restClient;

  private AccountService service;

  @Before
  public void setUp() {
    service = new AccountServiceImpl(restClient);
    ReflectionTestUtils.setField(service, "accountUrl", "http://www.google.com/accounts");
  }

  @Test
  public void testGetAccountDetails() {
    //arrange
    when(restClient.exchange(any(), any(), any(Class.class))).thenReturn(createAccount());

    //act
    Account account = service.getAccountDetails("1");

    //assert
    assertThat(account.getOwner(), equalTo(ACCOUNT_OWNER));
    assertThat(account.getBalance(), equalTo(BALANCE));
  }

  private Account createAccount() {
    Account account = new Account();
    account.setBalance(BALANCE);
    account.setOwner(ACCOUNT_OWNER);
    return account;
  }
}
