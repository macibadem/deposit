package com.deposit.apigateway.security.service;

import com.deposit.apigateway.client.CustomerClient;
import com.deposit.apigateway.client.dto.CustomerDto;
import com.deposit.apigateway.constants.CommonConstants.Modules;
import com.deposit.apigateway.security.properties.ClientAuthProperties;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements
    org.springframework.security.core.userdetails.UserDetailsService {

  private final CustomerClient customerClient;
  private final ClientAuthProperties clientAuthProperties;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var apiSecret = clientAuthProperties.getProperties().get(Modules.API_GATEWAY);
    var customerDto = customerClient.getCustomerByUsername(username, Modules.API_GATEWAY,
        apiSecret);
    return Optional.ofNullable(customerDto)
        .map(this::convert)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  private UserDetails convert(CustomerDto customerDto) {
    return User.builder()
        .username(customerDto.username())
        .password(customerDto.password())
        .authorities("USER")
        .build();
  }
}