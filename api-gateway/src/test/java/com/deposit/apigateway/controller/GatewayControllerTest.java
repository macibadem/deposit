package com.deposit.apigateway.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deposit.apigateway.client.AccountClient;
import com.deposit.apigateway.client.QueryClient;
import com.deposit.apigateway.security.properties.ClientAuthProperties;
import com.deposit.apigateway.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

@ExtendWith(MockitoExtension.class)
class GatewayControllerTest {

  @Mock
  private QueryClient queryClient;
  @Mock
  private HttpServletRequest request;
  @Mock
  private AccountClient accountClient;
  @InjectMocks
  private GatewayController gatewayController;
  @Mock
  private ClientAuthProperties clientAuthProperties;

  @Test
  void handleAccountRequests_whenPostRequestSend_thenForwardSuccess() {
    //given
    var accountPath = "vo/accounts/";
    var body = "{\"initialCredit\":\"10.0\"}";
    var headerMap = Map.of("x-api-key", "api-gateway");
    when(request.getMethod()).thenReturn("POST");
    when(accountClient.post(accountPath, body, headerMap))
        .thenReturn(ResponseEntity.ok("Success"));
    try (var httpUtilMock = mockStatic(HttpUtil.class);
        var requestMethodMock = mockStatic(RequestMethod.class)) {
      httpUtilMock.when(() -> HttpUtil.replaceGatewayAccountPath(request)).thenReturn(accountPath);
      httpUtilMock.when(() -> HttpUtil.appendCustomHeaders(request, clientAuthProperties))
          .thenReturn(headerMap);
      requestMethodMock.when(() -> RequestMethod.resolve("POST")).thenReturn(RequestMethod.POST);
      //when
      var response = gatewayController.handleAccountRequests(request, body);
      //then
      assertEquals(HttpStatus.OK, response.getStatusCode());
      verify(accountClient).post(accountPath, body, headerMap);
    }
  }

  @Test
  void handleAccountRequests_whenInvalidMethodType_thenReturnMethodNotAllowed() {
    //given
    var accountPath = "vo/accounts/";
    var body = "{\"initialCredit\":\"10.0\"}";
    var headerMap = Map.of("x-api-key", "api-gateway");
    when(request.getMethod()).thenReturn("GET");
    try (var httpUtilMock = mockStatic(HttpUtil.class);
        var requestMethodMock = mockStatic(RequestMethod.class)) {
      requestMethodMock.when(() -> RequestMethod.resolve("GET")).thenReturn(RequestMethod.GET);
      httpUtilMock.when(() -> HttpUtil.replaceGatewayAccountPath(request)).thenReturn(accountPath);
      httpUtilMock.when(() -> HttpUtil.appendCustomHeaders(request, clientAuthProperties))
          .thenReturn(headerMap);
      //when
      var response = gatewayController.handleAccountRequests(request, body);
      //then
      assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }
  }

  @Test
  void handleAccountRequests_whenExceptionThrown_thenReturnInternalServerError() {
    //given
    lenient().when(accountClient.post(anyString(), any(), any()))
        .thenThrow(new RuntimeException(""));
    //when
    var response = gatewayController.handleAccountRequests(request, null);
    //then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  void handleQueryRequests_whenGetRequestSend_thenForwardSuccess() {
    //given
    var queryPath = "vo/queries/user-info";
    var headerMap = Map.of("x-api-key", "api-gateway");
    when(request.getMethod()).thenReturn("GET");
    when(queryClient.get(queryPath, null, headerMap))
        .thenReturn(ResponseEntity.ok(getUserInfoString()));
    try (var httpUtilMock = mockStatic(HttpUtil.class);
        var requestMethodMock = mockStatic(RequestMethod.class)) {
      httpUtilMock.when(() -> HttpUtil.replaceGatewayQueryPath(request)).thenReturn(queryPath);
      httpUtilMock.when(() -> HttpUtil.appendCustomHeaders(request, clientAuthProperties))
          .thenReturn(headerMap);
      requestMethodMock.when(() -> RequestMethod.resolve("GET")).thenReturn(RequestMethod.GET);
      //when
      var response = gatewayController.handleQueryRequests(request, null);
      //then
      assertEquals(HttpStatus.OK, response.getStatusCode());
      verify(queryClient).get(queryPath, null, headerMap);
    }
  }

  @Test
  void handleQueryRequests_whenExceptionThrown_thenReturnInternalServerError() {
    //given
    lenient().when(queryClient.get(anyString(), any(), any()))
        .thenThrow(new RuntimeException(""));
    //when
    var response = gatewayController.handleQueryRequests(request, null);
    //then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  private String getUserInfoString() {
    return """
        {
          "name": "Mustafa",
          "surname": "Acibadem",
          "balance": 225.00,
          "accountInfoList": [
            {
              "accountId": 1,
              "transactions": [
                {
                  "accountId": 1,
                  "amount": 75.50,
                  "timestamp": "2024-12-11T10:15:30Z"
                },
                {
                  "accountId": 1,
                  "amount": 24.50,
                  "timestamp": "2024-12-12T12:00:00Z"
                }
              ]
            },
            {
              "accountId": 2,
              "transactions": [
                {
                  "accountId": 2,
                  "amount": 125.00,
                  "timestamp": "2024-12-10T08:00:00Z"
                }
              ]
            }
          ]
        }
        """;
  }
}