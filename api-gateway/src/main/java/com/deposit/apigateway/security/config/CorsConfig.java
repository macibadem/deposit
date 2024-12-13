package com.deposit.apigateway.security.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    var configSource = new UrlBasedCorsConfigurationSource();
    configSource.registerCorsConfiguration("/**", corsConfiguration());
    return configSource;
  }

  private CorsConfiguration corsConfiguration() {
    var corsConfig = new CorsConfiguration();
    corsConfig.setAllowedOrigins(List.of("http://localhost:3000"));
    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    corsConfig.setAllowedHeaders(List.of("*"));
    return corsConfig;
  }
}
