package com.deposit.customerservice.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  //handling other exception
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleUnhandledExceptions(final Exception ex) {
    log.error("Unhandled exception", ex);
    return ResponseEntity.internalServerError().body("exception occured");
  }
}