package com.deposit.apigateway.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deposit.apigateway.dto.auth.LoginRequest;
import com.deposit.apigateway.dto.auth.LoginResponse;
import com.deposit.apigateway.exception.GatewayException;
import com.deposit.apigateway.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

  private static final String TOKEN = "token";
  @Mock
  private AuthenticationService authenticationService;
  @InjectMocks
  private AuthenticationController authenticationController;

  @Test
  void login_success() throws GatewayException {
    //given
    var request = LoginRequest.builder()
        .username("admin")
        .password("123456")
        .build();
    var response = LoginResponse.builder().token(TOKEN).build();
    when(authenticationService.login(request)).thenReturn(response);
    //when
    var loginResponse = authenticationController.login(request);
    //then
    assertNotNull(loginResponse);
    assertEquals(TOKEN, loginResponse.getBody().token());
    verify(authenticationService, times(1)).login(request);
  }
}