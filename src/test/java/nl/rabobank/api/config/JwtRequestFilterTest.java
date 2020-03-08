package nl.rabobank.api.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import nl.rabobank.api.exception.InvalidTokenException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RunWith(MockitoJUnitRunner.class)
public class JwtRequestFilterTest {

  @Mock
  private JwtTokenProvider jwtTokenProvider;
  @Mock
  private FilterChain filterChain;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;

  private JwtRequestFilter filter;

  @Before
  public void setUp() {
    filter = new JwtRequestFilter(jwtTokenProvider);
  }

  @Test
  public void testDoFilter() throws InvalidTokenException, ServletException, IOException {
    // Arrange
    Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
    when(jwtTokenProvider.resolveToken(any())).thenReturn("token");
    when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
    when(jwtTokenProvider.getAuthentication(anyString())).thenReturn(authentication);

    // Act
    filter.doFilterInternal(request, response, filterChain);

    // Assert
    verify(jwtTokenProvider, times(1)).getAuthentication(anyString());
  }

  @Test
  public void testDoFilter_invalid_token() throws InvalidTokenException, ServletException, IOException {
    // Arrange
    Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
    when(jwtTokenProvider.resolveToken(any())).thenReturn("token");
    when(jwtTokenProvider.validateToken(anyString())).thenThrow(new InvalidTokenException(new Exception()));

    // Act
    filter.doFilterInternal(request, response, filterChain);

    // Assert
    verify(response, times(1)).sendError(anyInt(), anyString());
  }

}
