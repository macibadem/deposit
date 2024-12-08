package com.deposit.apigateway.security.filter;

import com.deposit.apigateway.security.service.JwtService;
import com.deposit.apigateway.util.HttpUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    final var jwt = HttpUtil.resolveBearerToken(request);

    if (StringUtils.hasText(jwt)) {
      final String username = jwtService.extractUsername(jwt);
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

      if (Objects.nonNull(userDetails) &&
          Objects.isNull(SecurityContextHolder.getContext().getAuthentication()) &&
          jwtService.isTokenValid(jwt, userDetails)) {
        var authToken = new UsernamePasswordAuthenticationToken(userDetails, jwt,
            userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
