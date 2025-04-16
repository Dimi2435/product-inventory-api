package com.example.productinventory.exception;

/**
 * A class representing an error response. This class is used to encapsulate error messages returned
 * to the client.
 */
public class ErrorResponse {
  private String message; // The error message to be returned

  /**
   * Constructs an ErrorResponse with the specified message.
   *
   * @param message the error message
   */
  public ErrorResponse(String message) {
    this.message = message;
  }

  /**
   * Returns the error message.
   *
   * @return the error message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the error message.
   *
   * @param message the error message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }
}
