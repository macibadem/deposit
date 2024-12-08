package com.deposit.apigateway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deposit.apigateway.client.CustomerClient;
import com.deposit.apigateway.client.dto.CustomerDto;
import com.deposit.apigateway.constants.CommonConstants.Modules;
import com.deposit.apigateway.dto.auth.LoginRequest;
import com.deposit.apigateway.exception.GatewayException;
import com.deposit.apigateway.security.properties.ClientAuthProperties;
import com.deposit.apigateway.security.service.JwtService;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

  private static final String TOKEN = "token";
  private static final String ADMIN_USERNAME = "admin";
  private static final String ADMIN_PASSWORD = "123456";
  private static final String ADMIN_WRONG_PASSWORD = "xxxx";
  private static final String API_GATEWAY_SECRET = "secret";

  @Mock
  private JwtService jwtService;
  @Mock
  private CustomerClient customerClient;
  @Mock
  private ClientAuthProperties clientAuthProperties;
  @Mock
  private AuthenticationManager authenticationManager;
  @InjectMocks
  private AuthenticationService authenticationService;

  @BeforeEach
  void setUp() {
    ClientAuthProperties authProps = new ClientAuthProperties();
    authProps.setProperties(Map.of(Modules.API_GATEWAY, API_GATEWAY_SECRET));
    when(clientAuthProperties.getProperties()).thenReturn(authProps.getProperties());
  }

  @Test
  void login_success() throws GatewayException {
    //given
    var customerDto = CustomerDto.builder()
        .username(ADMIN_USERNAME)
        .password(ADMIN_PASSWORD)
        .build();
    var authentication = new UsernamePasswordAuthenticationToken(ADMIN_USERNAME, ADMIN_PASSWORD);
    when(jwtService.generateToken(ADMIN_USERNAME)).thenReturn(TOKEN);
    when(customerClient.getCustomerByUsername(customerDto.username(), Modules.API_GATEWAY,
        API_GATEWAY_SECRET)).thenReturn(customerDto);
    when(authenticationManager.authenticate(authentication)).thenReturn(authentication);
    //when
    var response = authenticationService.login(new LoginRequest(ADMIN_USERNAME, ADMIN_PASSWORD));
    //then
    assertNotNull(response);
    assertEquals(TOKEN, response.token());
  }


  @Test
  void login_whenUserNotFound_thenThrowException() {
    //given
    when(customerClient.getCustomerByUsername(anyString(), anyString(), anyString())).thenReturn(
        null);
    //when
    var exception = assertThrows(GatewayException.class,
        () -> authenticationService.login(new LoginRequest(ADMIN_USERNAME, ADMIN_PASSWORD)));
    //then
    assertEquals("USER_NOT_FOUND", exception.getCode());
    verify(customerClient).getCustomerByUsername(anyString(), anyString(), anyString());
  }


  @Test
  void login_whenAuthenticationFails_thenThrowException() {
    //given
    var customerDto = CustomerDto.builder()
        .username(ADMIN_USERNAME)
        .password(ADMIN_WRONG_PASSWORD)
        .build();
    var authentication = new UsernamePasswordAuthenticationToken(ADMIN_USERNAME,
        ADMIN_WRONG_PASSWORD);
    when(customerClient.getCustomerByUsername(customerDto.username(), Modules.API_GATEWAY,
        API_GATEWAY_SECRET)).thenReturn(customerDto);
    doThrow(new RuntimeException("bad creds")).when(authenticationManager)
        .authenticate(authentication);
    //when
    var exception = assertThrows(RuntimeException.class,
        () -> authenticationService.login(new LoginRequest(ADMIN_USERNAME, ADMIN_WRONG_PASSWORD)));
    //then
    assertTrue(exception.getMessage().contains("bad creds"));
    verify(customerClient).getCustomerByUsername(customerDto.username(), Modules.API_GATEWAY,
        API_GATEWAY_SECRET);
  }
}