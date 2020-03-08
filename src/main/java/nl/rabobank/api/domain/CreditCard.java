package nl.rabobank.api.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditCard {

  private String id;
  private Status status;
  private long cardNumber;
  private long sequenceNumber;
  private String cardHolder;
  private BigDecimal monthlyLimit;

}
