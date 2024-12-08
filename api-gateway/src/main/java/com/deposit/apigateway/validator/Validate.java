package com.deposit.apigateway.validator;

import com.deposit.apigateway.exception.GatewayException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Validate {
  public static void stateNot(boolean falseExpectedExpression, String errorCode)
      throws GatewayException {
    if (falseExpectedExpression) {
      throw new GatewayException(errorCode);
    }
  }

  public static void state(boolean trueExpectedExpression, String errorCode)
      throws GatewayException {
    stateNot(!trueExpectedExpression, errorCode);
  }
}
