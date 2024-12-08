package com.deposit.apigateway.exception;

import lombok.Getter;

@Getter
public class GatewayException extends Exception {
  private final String code;

  public GatewayException(final String code, final String message) {
    super(message);
    this.code = code;
  }

  public GatewayException(final String code) {
    super();
    this.code = code;
  }
}