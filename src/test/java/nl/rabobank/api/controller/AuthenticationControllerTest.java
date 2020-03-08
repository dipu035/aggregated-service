package nl.rabobank.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.api.domain.TokenRequest;
import nl.rabobank.api.exception.handler.ControllerAdvice;
import nl.rabobank.api.service.AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationControllerTest {

  @Mock
  private AuthenticationService service;

  private MockMvc mockMvc;

  @Before
  public void setUp() {
    AuthenticationController controller = new AuthenticationController(service);
    this.mockMvc = standaloneSetup(controller).setControllerAdvice(new ControllerAdvice()).build();
  }

  @Test
  public void test_generate_token_OK() throws Exception {
    //arrange
    when(service.generateToken(any())).thenReturn("token");

    //Act and assert
    this.mockMvc.perform(post("/api/generate-token")
        .content(createRequest())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
  }

  private String createRequest() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(new TokenRequest("username", "passwrod"));
  }
}
