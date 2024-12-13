package com.deposit.apigateway.config;


import com.deposit.apigateway.dto.ResultDto;
import com.deposit.apigateway.exception.GatewayException;
import feign.FeignException;
import java.net.ConnectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(GatewayException.class)
  public ResponseEntity<Object> handleAssignmentException(final GatewayException e) {
    var resultDto = ResultDto.of(e.getCode(), e.getMessage());
    return ResponseEntity.internalServerError().body(resultDto);
  }

  @ExceptionHandler(ConnectException.class)
  public ResponseEntity<ResultDto> handleConnectException(ConnectException e) {
    var result = ResultDto.of("Gateway-001", e.getMessage());
    return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ResultDto> handleIllegalArgumentException(IllegalArgumentException e) {
    var resultDto = ResultDto.of("Gateway-002", e.getMessage());
    return ResponseEntity.badRequest().body(resultDto);
  }

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<ResultDto> handleFeignException(FeignException e) {
    HttpStatus status = HttpStatus.resolve(e.status());
    var result = ResultDto.of("Gateway-003", e.getMessage());
    return ResponseEntity.status(status != null ?
            status :
            HttpStatus.INTERNAL_SERVER_ERROR)
        .body(result);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResultDto> handleException(Exception e) {
    var resultDto = ResultDto.of("Gateway-004", e.getMessage());
    return ResponseEntity.internalServerError().body(resultDto);
  }
}
