package nl.rabobank.api.config;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static nl.rabobank.api.config.SecurityConstants.HEADER_STRING;
import static nl.rabobank.api.config.SecurityConstants.JWT_SECRET;
import static nl.rabobank.api.config.SecurityConstants.TOKEN_PREFIX;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.api.exception.InvalidTokenException;
import nl.rabobank.api.service.impl.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

  @Value("${security.jwt.token.expire-length:3600000}")
  private long validityInMilliseconds; // 1h

  private final UserService userService;

  public String createToken(String username, String... roles) {

    Claims claims = Jwts.claims().setSubject(username);
    claims
        .put("auth",
            Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
        .setClaims(claims)
        .signWith(hmacShaKeyFor(JWT_SECRET.getBytes()), SignatureAlgorithm.HS512)
        .setIssuedAt(now)
        .setExpiration(validity)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userService.loadUserByUsername(getUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    return Jwts.parser().setSigningKey(JWT_SECRET.getBytes()).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader(HEADER_STRING);
    if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
      return bearerToken.replace(TOKEN_PREFIX, "");
    }
    return null;
  }

  public boolean validateToken(String token) throws InvalidTokenException {
    try {
      Jwts.parser().setSigningKey(JWT_SECRET.getBytes()).parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      log.warn("Expired or invalid JWT token", e);
      throw new InvalidTokenException(e);
    }
  }

}