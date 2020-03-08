package nl.rabobank.api.service;

import nl.rabobank.api.domain.Account;

public interface AccountService {
  Account getAccountDetails(String id);
}
