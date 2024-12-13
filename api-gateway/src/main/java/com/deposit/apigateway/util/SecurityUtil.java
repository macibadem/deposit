package com.deposit.apigateway.util;

import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtil {

  public static String extractAuthenticatedUser() {
    return Optional.ofNullable(
            SecurityContextHolder.getContext().getAuthentication())
        .map(Authentication::getName)
        .map(Object::toString)
        .orElse(null);
  }
}
