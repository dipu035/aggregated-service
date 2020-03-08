package nl.rabobank.api.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  private static final String USERNAME = "admin";
  private static final String PASSWORD = "secret";
  private static final String ROLE = "admin";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private PasswordEncoder passwordEncoder;

  private UserService service;

  @Before
  public void setUp() {
    service = new UserService(passwordEncoder);
    setFields();
    when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
    service.init();
  }

  @Test
  public void testLoadUserByUsername() {
    //act
    UserDetails userDetails = service.loadUserByUsername(USERNAME);

    //assert
    assertThat(userDetails.getUsername(), equalTo(USERNAME));
    assertThat(userDetails.getPassword(), equalTo("encoded_password"));
    assertThat(userDetails.getAuthorities().size(), equalTo(1));
    Optional<String> authorityOptional = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority).findFirst();
    assertTrue(authorityOptional.isPresent());
    assertThat(authorityOptional.get(), equalTo(ROLE));
  }

  @Test
  public void testLoadUserByUsername_not_found() {
    //act & assert
    thrown.expect(UsernameNotFoundException.class);
    thrown.expectMessage("The user with username: Unknown not found");
    service.loadUserByUsername("Unknown");
  }

  private void setFields() {
    ReflectionTestUtils.setField(service, "role", ROLE);
    ReflectionTestUtils.setField(service, "username", USERNAME);
    ReflectionTestUtils.setField(service, "password", PASSWORD);
  }
}
