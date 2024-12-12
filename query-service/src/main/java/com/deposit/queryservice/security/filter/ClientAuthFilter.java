package com.deposit.queryservice.security.filter;


import com.deposit.queryservice.constants.CommonConstants.Header;
import com.deposit.queryservice.security.properties.ClientAuthProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class ClientAuthFilter extends OncePerRequestFilter {

  @Value("${server.servlet.context-path}")
  private String contextPath;

  private static final List<String> PATH_WHITE_LIST = List.of(
      "/swagger-ui.html",
      "/v3/api-docs",
      "/swagger-ui/**",
      "/actuator/**",
      "/h2-console/**");


  private final ClientAuthProperties clientAuthProperties;
  private final AntPathMatcher antPathMatcher = new AntPathMatcher();

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain chain) throws ServletException, IOException {
    String uri = request.getRequestURI();

    if (PATH_WHITE_LIST.parallelStream()
        .anyMatch(path -> antPathMatcher.match(contextPath.concat(path), uri))) {
      chain.doFilter(request, response);
      return;
    }

    String client = request.getHeader(Header.API_KEY);
    String secret = request.getHeader(Header.API_SECRET);

    try {
      if (isClientNotAuthenticated(client, secret)) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        return;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    chain.doFilter(request, response);
  }

  private boolean isClientNotAuthenticated(final String client, final String secret) {
    var secretProp = clientAuthProperties.getProperties().get(client);
    return !StringUtils.hasText(secretProp) || !secret.equals(secretProp);
  }
}
