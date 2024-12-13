package com.deposit.accountservice.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class CommonConstants {

  @UtilityClass
  public class Header {
    public final static String API_KEY = "x-api-key";
    public final static String API_SECRET = "x-api-secret";
    public final static String CUSTOMER_ID = "x-customer-id";
  }

  @UtilityClass
  public class Modules {
    public final static String API_GATEWAY = "api-gateway";
  }

  @UtilityClass
  public class Topics {
    public final static String ACCOUNT_CREATED = "account-created";
  }
}
