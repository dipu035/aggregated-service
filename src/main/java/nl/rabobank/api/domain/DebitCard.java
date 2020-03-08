package nl.rabobank.api.domain;

import lombok.Data;

@Data
public class DebitCard {

  private String id;
  private long cardNumber;
  private long sequenceNumber;
  private String cardHolder;
  private CardLimit atmLimit;
  private CardLimit posLimit;
  private boolean contactless;
  private Status status;
}
