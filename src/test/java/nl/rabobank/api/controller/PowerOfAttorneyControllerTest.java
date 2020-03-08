package nl.rabobank.api.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import nl.rabobank.api.domain.PowerOfAttorneyDetails;
import nl.rabobank.api.exception.NotFoundException;
import nl.rabobank.api.exception.handler.ControllerAdvice;
import nl.rabobank.api.service.PowerOfAttorneyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(MockitoJUnitRunner.class)
public class PowerOfAttorneyControllerTest {

  @Mock
  private PowerOfAttorneyService service;

  private MockMvc mockMvc;

  @Before
  public void setUp() {
    PowerOfAttorneyController controller = new PowerOfAttorneyController(service);
    this.mockMvc = standaloneSetup(controller).setControllerAdvice(new ControllerAdvice()).build();
  }

  @Test
  public void test_poa_ById_OK() throws Exception {
    //arrange
    when(service.getPowerOfAttorneyDetails(anyString()))
        .thenReturn(new PowerOfAttorneyDetails());

    //Act and assert
    this.mockMvc.perform(get(constructUrl("10"))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
  }

  @Test
  public void test_poa_ById_Not_Found() throws Exception {
    //arrange
    String notFoundMessage = "requested card is not found";
    when(service.getPowerOfAttorneyDetails(anyString()))
        .thenThrow(new NotFoundException(notFoundMessage));

    //Act and assert
    this.mockMvc.perform(get(constructUrl("10"))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNotFound())
        .andExpect(content().string(notFoundMessage));
  }

  private String constructUrl(String id) {
    String path = "/api/admin/power-of-attorneys";
    if (id == null) {
      return path;
    }
    return path + "/" + id;
  }
}
