package com.example.productinventory.exception;

import java.time.LocalDateTime;
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

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    // Map<String, String> response = new HashMap<>();
    // response.put("message", ex.getMessage());
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.get("message"));
  }

  @ExceptionHandler(ProductOptimisticLockException.class)
  public ResponseEntity<String> handleOptimisticLockException(ProductOptimisticLockException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  @ExceptionHandler(ProductBadRequestException.class)
  public ResponseEntity<Object> handleProductBadRequestException(
      ProductBadRequestException ex, WebRequest request) {
    logger.error(
        "ProductBadRequestException: Bad request exception occurred: {}", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
  }

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

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An unexpected error occurred.");
  }

  // private ResponseEntity<Map<String, String>> createErrorResponse(
  //     HttpStatus status, String message) {
  //   Map<String, String> errorResponse = new HashMap<>();
  //   errorResponse.put("message", message);
  //   return ResponseEntity.status(status).body(errorResponse);
  // }
}
