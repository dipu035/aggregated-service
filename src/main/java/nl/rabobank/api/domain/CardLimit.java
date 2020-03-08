package nl.rabobank.api.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardLimit {

  private BigDecimal limit;
  private PeriodUnit periodUnit;
}
