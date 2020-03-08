package nl.rabobank.api.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PowerOfAttorney {

  private String id;
  private String grantor;
  private String grantee;
  private String account;
  private Direction direction;
  private List<Authorization> authorizations = new ArrayList<>();
  private List<CardReference> cards = new ArrayList<>();

}
