package nl.rabobank.api.config;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import nl.rabobank.api.exception.InvalidTokenException;
import nl.rabobank.api.service.impl.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenProviderTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private UserService userService;
  @Mock
  private HttpServletRequest httpServletRequest;

  private JwtTokenProvider tokenProvider;

  @Before
  public void setUp() {
    tokenProvider = new JwtTokenProvider(userService);
    ReflectionTestUtils.setField(tokenProvider, "validityInMilliseconds", 3600000);
  }

  @Test
  public void testCreateToken() throws InvalidTokenException {
    //act
    String token = tokenProvider.createToken("admin", "admin");
    System.out.println(token);

    //assert
    //validate token
    boolean isValid = tokenProvider.validateToken(token);
    assertThat(isValid, equalTo(true));
    //validate username
    String parsedUsernameFromToken = tokenProvider.getUsername(token);
    assertThat(parsedUsernameFromToken, equalTo("admin"));
  }

  @Test
  public void testValidateToken_invalid_token() throws InvalidTokenException {
    //act & assert
    String token = "invalid_token";
    thrown.expect(InvalidTokenException.class);
    tokenProvider.validateToken(token);
  }

  @Test
  public void testResolveToken() {
    //arrange
    when(httpServletRequest.getHeader(anyString())).thenReturn("Bearer bearer_token");

    //act
    String token = tokenProvider.resolveToken(httpServletRequest);

    //assert
    assertThat(token, equalTo("bearer_token"));
  }

  @Test
  public void testResolveToken_null() {
    //arrange
    when(httpServletRequest.getHeader(anyString())).thenReturn(null);

    //act
    String token = tokenProvider.resolveToken(httpServletRequest);

    //assert
    assertNull(token);
  }

  @Test
  public void testGetAuthentication() {
    //arrange
    String username = "admin";
    UserDetails userDetails = createUserDetails(username);
    String token = tokenProvider.createToken(username, "admin");
    when(userService.loadUserByUsername(anyString())).thenReturn(userDetails);

    //act
    Authentication authentication = tokenProvider.getAuthentication(token);

    //assert
    User user = (User) authentication.getPrincipal();
    assertThat(user.getUsername(), equalTo(username));
  }

  private UserDetails createUserDetails(String username) {
    return User.withUsername(username).password("password").authorities("admin").build();
  }

}
