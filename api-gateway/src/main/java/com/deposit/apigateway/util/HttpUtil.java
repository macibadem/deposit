package com.deposit.apigateway.util;

import com.deposit.apigateway.constants.CommonConstants.Header;
import com.deposit.apigateway.constants.CommonConstants.Modules;
import com.deposit.apigateway.security.properties.ClientAuthProperties;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

@UtilityClass
public class HttpUtil {

  private static final String GATEWAY_QUERY_PATH = "/api/gateway/query/";
  private static final String GATEWAY_ACCOUNT_PATH = "/api/gateway/account/";

  public static String resolveBearerToken(final HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
        .filter(StringUtils::hasText)
        .filter(header -> header.startsWith("Bearer "))
        .map(header -> header.substring(7))
        .orElse(null);
  }

  public static Map<String, String> appendCustomHeaders(HttpServletRequest request,
      ClientAuthProperties clientAuthProperties) {
    var headerMap = Collections.list(request.getHeaderNames()).stream()
        .collect(Collectors.toMap(
            headerName -> headerName, request::getHeader
        ));

    headerMap.put(Header.API_KEY, Modules.API_GATEWAY);
    headerMap.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headerMap.put(Header.CUSTOMER_ID, SecurityUtil.extractAuthenticatedUser());
    headerMap.put(Header.API_SECRET, clientAuthProperties.getProperties().get(Modules.API_GATEWAY));

    return headerMap;
  }

  public static String replaceGatewayAccountPath(HttpServletRequest request) {
    return request.getRequestURI().replace(GATEWAY_ACCOUNT_PATH, "");
  }

  public static String replaceGatewayQueryPath(HttpServletRequest request) {
    return request.getRequestURI().replace(GATEWAY_QUERY_PATH, "");
  }
}
