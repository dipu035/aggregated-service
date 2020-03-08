package nl.rabobank.api.exception;

public class InvalidTokenException extends Exception {

  public InvalidTokenException(Exception e) {
    super(e);
  }
}
