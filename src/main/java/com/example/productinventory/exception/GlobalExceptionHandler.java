package com.example.productinventory.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for the application. Provides consistent error responses for all
 * exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Handles ProductConflictException and returns a 409 Conflict response.
   *
   * @param ex the ProductConflictException that was thrown
   * @return ResponseEntity containing the error message and HTTP status 409 (Conflict)
   */
  @ExceptionHandler(ProductConflictException.class)
  public ResponseEntity<Object> handleConflictException(
      ProductConflictException ex, WebRequest request) {
    logger.error("Conflict exception occurred: {}", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex, WebRequest request) {
    logger.error("Bad request exception occurred: {}", ex.getMessage(), ex);

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Bad Request");
    body.put("message", "Malformed JSON request");
    body.put("path", request.getDescription(false).replace("uri=", ""));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
  }

  /**
   * Handles MethodArgumentNotValidException and returns a 400 Bad Request response.
   *
   * <p>This method is invoked when validation on an argument annotated with @Valid fails. It
   * collects validation error messages and returns them in the response body.
   *
   * @param ex the MethodArgumentNotValidException that was thrown
   * @param request the WebRequest object containing request details
   * @return ResponseEntity containing the error details and HTTP status 400 (Bad Request)
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(
      MethodArgumentNotValidException ex, WebRequest request) {
    logger.error("Validation failed: {}", ex.getMessage(), ex);

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Validation Error");
    body.put("message", "Validation failed for the request");

    Map<String, String> errors = new LinkedHashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.put(error.getField(), error.getDefaultMessage());
    }
    body.put("errors", errors);
    body.put("path", request.getDescription(false).replace("uri=", ""));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  /**
   * Handles ProductUnprocessableEntityException and returns a 422 Unprocessable Entity response.
   *
   * @param ex the ProductUnprocessableEntityException that was thrown
   * @param request the WebRequest object containing request details
   * @return ResponseEntity containing the error details and HTTP status 422 (Unprocessable Entity)
   */
  @ExceptionHandler(ProductUnprocessableEntityException.class)
  public ResponseEntity<Object> handleProductUnprocessableEntityException(
      ProductUnprocessableEntityException ex, WebRequest request) {
    logger.error("Unprocessable entity exception occurred: {}", ex.getMessage(), ex);

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
    body.put("error", "Unprocessable Entity");
    body.put("message", ex.getMessage());
    body.put("path", request.getDescription(false).replace("uri=", ""));

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
  }

  /**
   * Handles ProductNotFoundException and returns a 404 Not Found response.
   *
   * @param ex the ProductNotFoundException that was thrown
   * @return ResponseEntity containing the error message and HTTP status 404 (Not Found)
   */
  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  /**
   * Handles ProductOptimisticLockException and returns a 409 Conflict response.
   *
   * @param ex the ProductOptimisticLockException that was thrown
   * @return ResponseEntity containing the error message and HTTP status 409 (Conflict)
   */
  @ExceptionHandler(ProductOptimisticLockException.class)
  public ResponseEntity<Object> handleOptimisticLockException(ProductOptimisticLockException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  /**
   * Handles ProductBadRequestException and returns a 400 Bad Request response.
   *
   * @param ex the ProductBadRequestException that was thrown
   * @return ResponseEntity containing the error message and HTTP status 400 (Bad Request)
   */
  @ExceptionHandler(ProductBadRequestException.class)
  public ResponseEntity<Object> handleProductBadRequestException(
      ProductBadRequestException ex, WebRequest request) {
    logger.error(
        "ProductBadRequestException: Bad request exception occurred: {}", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
  }

  /**
   * Handles ProductException and returns a response based on the specific exception type.
   *
   * <p>This method is invoked when a ProductException is thrown. It logs the error and returns a
   * structured error response to the client, including details such as the error code and message.
   *
   * @param ex the ProductException that was thrown
   * @param request the WebRequest object containing request details
   * @return ResponseEntity containing the error details and the appropriate HTTP status
   */
  @ExceptionHandler(ProductException.class)
  public ResponseEntity<Object> handleProductException(ProductException ex, WebRequest request) {
    logger.error("Product exception occurred: {}", ex.getMessage(), ex);

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", ex.getStatus().value());
    body.put("error", ex.getStatus().getReasonPhrase());
    body.put("message", ex.getMessage());
    body.put("errorCode", ex.getErrorCode());
    body.put("details", ex.getDetails());
    body.put("path", request.getDescription(false).replace("uri=", ""));

    return new ResponseEntity<>(body, ex.getStatus());
  }

  /**
   * Handles ProductInternalServerErrorException and returns a 500 Internal Server Error response.
   *
   * <p>This method is invoked when an internal server error occurs related to product operations.
   * It logs the error and returns a structured error response to the client.
   *
   * @param ex the ProductInternalServerErrorException that was thrown
   * @param request the WebRequest object containing request details
   * @return ResponseEntity containing the error details and HTTP status 500 (Internal Server Error)
   */
  @ExceptionHandler(ProductInternalServerErrorException.class)
  public ResponseEntity<Object> handleProductInternalServerErrorException(
      ProductInternalServerErrorException ex, WebRequest request) {
    logger.error("Internal server error: {}", ex.getMessage(), ex);

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    body.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    body.put("message", "Internal server error");
    body.put("path", request.getDescription(false).replace("uri=", ""));

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(ex.getMessage()));
  }

  /**
   * Handles generic exceptions and returns a 500 Internal Server Error response.
   *
   * @param ex the Exception that was thrown
   * @return ResponseEntity containing a generic error message and HTTP status 500 (Internal Server
   *     Error)
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An unexpected error occurred.");
  }
}
