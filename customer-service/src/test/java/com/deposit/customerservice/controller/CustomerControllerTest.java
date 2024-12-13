package com.deposit.customerservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deposit.customerservice.dto.CustomerDto;
import com.deposit.customerservice.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

  public static final String ADMIN = "admin";
  public static final String NOT_EXISTS_USER = "notExistsUser";

  public static final Long ADMIN_ID = 1L;
  public static final Long NOT_EXISTS_CUSTOMER_ID = -1L;
  
  @Mock
  private CustomerService customerService;
  @InjectMocks
  private CustomerController customerController;

  @Test
  void getByUsername_whenUserNotFound_thenReturnNoContent() {
    //given
    when(customerService.getByUsername(NOT_EXISTS_USER)).thenReturn(null);
    //when
    var response = customerController.getByUsername(NOT_EXISTS_USER);
    //then
    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(customerService, times(1)).getByUsername(NOT_EXISTS_USER);
  }

  @Test
  void getByUsername_whenUserFound_thenReturnSuccess() {
    //given
    var customerDto = CustomerDto.builder().username(ADMIN).build();
    when(customerService.getByUsername(ADMIN)).thenReturn(customerDto);
    //when
    var response = customerController.getByUsername(ADMIN);
    //then
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(ADMIN, response.getBody().username());
    verify(customerService, times(1)).getByUsername(ADMIN);
  }

  @Test
  void getByCustomerId_whenUserNotFound_thenReturnNoContent() {
    //given
    when(customerService.getByCustomerId(NOT_EXISTS_CUSTOMER_ID)).thenReturn(null);
    //when
    var response = customerController.getByCustomerId(NOT_EXISTS_CUSTOMER_ID);
    //then
    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(customerService, times(1)).getByCustomerId(NOT_EXISTS_CUSTOMER_ID);
  }

  @Test
  void getByCustomerId_whenUserFound_thenReturnSuccess() {
    //given
    var customerDto = CustomerDto.builder().username(ADMIN).build();
    when(customerService.getByCustomerId(ADMIN_ID)).thenReturn(customerDto);
    //when
    var response = customerController.getByCustomerId(ADMIN_ID);
    //then
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(ADMIN, response.getBody().username());
    verify(customerService, times(1)).getByCustomerId(ADMIN_ID);
  }
}