package com.deposit.accountservice.config;

import com.deposit.accountservice.dto.ResultDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  private static final String ERROR_CODE = "-1";

  //handling body validation exception
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach((error) -> {
      errors.put(((FieldError) error).getField(), error.getDefaultMessage());
    });

    var resultDto = ResultDto.of(ERROR_CODE, errors.toString());
    return ResponseEntity.badRequest().body(resultDto);
  }

  //handling param validation exception
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolationExceptions(
      ConstraintViolationException ex) {
    Map<String, String> errors = ex.getConstraintViolations().stream()
        .collect(Collectors.toMap(
            violation -> violation.getPropertyPath().toString(),
            ConstraintViolation::getMessage
        ));

    var resultDto = ResultDto.of(ERROR_CODE, errors.toString());
    return ResponseEntity.badRequest().body(resultDto);
  }

  //handling other exception
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleUnhandledExceptions(final Exception ex) {
    log.error("Unhandled exception", ex);
    var resultDto = ResultDto.of(ERROR_CODE, "unknown system error");
    return ResponseEntity.internalServerError().body(resultDto);
  }
}