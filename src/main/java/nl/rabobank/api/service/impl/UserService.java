package nl.rabobank.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private static final Map<String, UserDetails> USER_MAP = new HashMap<>();

  private final PasswordEncoder passwordEncoder;

  @Value("${user.username:admin}")
  private String username;

  @Value("${user.password:secret}")
  private String password;

  @Value("${user.role:ADMIN}")
  private String role;

  @PostConstruct
  public void init() {
    UserDetails admin = User.withUsername(username).password(passwordEncoder.encode(password))
        .authorities(role).build();
    USER_MAP.put(username, admin);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (!USER_MAP.containsKey(username)) {
      String message = "The user with username: %s not found";
      throw new UsernameNotFoundException(String.format(message, username));
    }
    return USER_MAP.get(username);

  }
}
