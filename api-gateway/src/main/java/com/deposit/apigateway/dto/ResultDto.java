package com.deposit.apigateway.dto;

import java.time.LocalDateTime;


public record ResultDto(String code, String message, String timestamp) {

  public static ResultDto of(String code, String message) {
    return new ResultDto(code, message, LocalDateTime.now().toString());
  }
}