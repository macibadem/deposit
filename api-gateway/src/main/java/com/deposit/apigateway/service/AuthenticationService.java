package com.deposit.apigateway.service;

import com.deposit.apigateway.client.CustomerClient;
import com.deposit.apigateway.constants.CommonConstants.Modules;
import com.deposit.apigateway.dto.auth.LoginRequest;
import com.deposit.apigateway.dto.auth.LoginResponse;
import com.deposit.apigateway.exception.GatewayException;
import com.deposit.apigateway.security.properties.ClientAuthProperties;
import com.deposit.apigateway.security.service.JwtService;
import com.deposit.apigateway.validator.Validate;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final JwtService jwtService;
  private final CustomerClient customerClient;
  private final ClientAuthProperties clientAuthProperties;
  private final AuthenticationManager authenticationManager;


  public LoginResponse login(LoginRequest request) throws GatewayException {
    var apiSecret = clientAuthProperties.getProperties().get(Modules.API_GATEWAY);
    var customer = customerClient.getCustomerByUsername(request.username(), Modules.API_GATEWAY,
        apiSecret);
    Validate.state(Objects.nonNull(customer), "USER_NOT_FOUND");
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        customer.username(),
        request.password()));
    return new LoginResponse(jwtService.generateToken(customer.username()));
  }
}