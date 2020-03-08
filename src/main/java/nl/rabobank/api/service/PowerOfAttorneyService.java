package nl.rabobank.api.service;

import nl.rabobank.api.domain.PowerOfAttorneyDetails;

public interface PowerOfAttorneyService {

  /**
   * Get the detailed information about the power of attorney based on the given id.
   * @param id The unique identifier of the power of attorney.
   * @return <code>PowerOfAttorneyDetails</code>
   */
  PowerOfAttorneyDetails getPowerOfAttorneyDetails(String id);
}
