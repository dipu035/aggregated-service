package nl.rabobank.api.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PowerOfAttorneyDetails {

  private String id;
  private String grantor;
  private String grantee;
  private Account accountDetails;
  private Direction direction;
  private List<Authorization> authorizations = new ArrayList<>();
  private List<DebitCard> debitCards = new ArrayList<>();
  private List<CreditCard> creditCards = new ArrayList<>();
}
