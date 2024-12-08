package com.deposit.customerservice.security.filter;


import com.deposit.customerservice.security.properties.ClientAuthProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class ClientAuthFilter extends OncePerRequestFilter {

  private static final List<String> PATH_WHITE_LIST = List.of(
      "/swagger-ui.html",
      "/v3/api-docs",
      "/swagger-ui/**",
      "/actuator/**",
      "/h2-console/**");

  private final static String HEADER_API_KEY = "x-api-key";
  private final static String HEADER_API_SECRET = "x-api-secret";


  private final ClientAuthProperties clientAuthProperties;
  private final AntPathMatcher antPathMatcher = new AntPathMatcher();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    String uri = request.getRequestURI();

    if (PATH_WHITE_LIST.parallelStream().anyMatch(path -> antPathMatcher.match(path, uri))) {
      chain.doFilter(request, response);
      return;
    }

    String client = request.getHeader(HEADER_API_KEY);
    String secret = request.getHeader(HEADER_API_SECRET);

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

    private boolean isClientNotAuthenticated(final String client, final String secret){
      var secretProp = clientAuthProperties.getProperties().get(client);
    return !StringUtils.hasText(secretProp) || !secret.equals(secretProp);
  }
}
