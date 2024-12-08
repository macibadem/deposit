package com.deposit.apigateway.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

@UtilityClass
public class HttpUtil {
  public static String resolveBearerToken(final HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
        .filter(StringUtils::hasText)
        .filter(header -> header.startsWith("Bearer "))
        .map(header -> header.substring(7))
        .orElse(null);
  }
}
