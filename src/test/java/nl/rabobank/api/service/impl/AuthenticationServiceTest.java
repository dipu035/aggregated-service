package nl.rabobank.api.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import nl.rabobank.api.config.JwtTokenProvider;
import nl.rabobank.api.domain.TokenRequest;
import nl.rabobank.api.service.AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

  @Mock
  private JwtTokenProvider jwtTokenProvider;
  @Mock
  private AuthenticationManager authenticationManager;

  private AuthenticationService service;

  @Before
  public void setUp() {
    service = new AuthenticationServiceImpl(jwtTokenProvider, authenticationManager);
    ReflectionTestUtils.setField(service, "role", "admin");
  }

  @Test
  public void testGenerateToken() {
    //arrange
    String username = "username";
    String password = "password";
    when(authenticationManager.authenticate(any()))
        .thenReturn(new UsernamePasswordAuthenticationToken(username, password));
    when(jwtTokenProvider.createToken(username, "admin")).thenReturn("token");

    //act
    String token = service.generateToken(new TokenRequest(username, password));

    //assert
    assertThat(token, equalTo("token"));
  }
}
