package nl.rabobank.api.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {

  private String owner;
  private BigDecimal balance;
  private String created;
}
