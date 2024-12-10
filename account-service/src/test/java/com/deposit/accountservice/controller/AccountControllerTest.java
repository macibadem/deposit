package com.deposit.accountservice.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import com.deposit.accountservice.dto.CreateAccountRequest;
import com.deposit.accountservice.service.AccountService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

  @Mock
  private AccountService accountService;
  @InjectMocks
  private AccountController accountController;

  @Test
  void createAccount_whenValidRequest_thenReturnsCreated() {
    //given
    var customerId = 1L;
    var request = new CreateAccountRequest(new BigDecimal("10.0"));
    //when
    var response = accountController.createAccount(customerId, request);
    //then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    verify(accountService).createAccount(customerId, request.initialCredit());
  }
}