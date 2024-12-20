package com.deposit.customerservice.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deposit.customerservice.dto.CustomerDto;
import com.deposit.customerservice.entity.Customer;
import com.deposit.customerservice.mapper.CustomerMapper;
import com.deposit.customerservice.repository.CustomerRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

  public static final String ADMIN = "admin";
  public static final String NOT_EXISTS_USER = "notExistsUser";
  public static final Long ADMIN_ID = 1L;
  public static final Long NOT_EXISTS_CUSTOMER_ID = -1L;

  @InjectMocks
  private CustomerService customerService;
  @Mock
  private CustomerRepository customerRepository;


  @Test
  void getByUsername_whenCustomerNotFound_thenReturnNoContent() {
    //given
    when(customerRepository.findByUsername(NOT_EXISTS_USER)).thenReturn(Optional.empty());
    //when
    var customerDto = customerService.getByUsername(NOT_EXISTS_USER);
    //then
    assertNull(customerDto);
    verify(customerRepository, times(1)).findByUsername(NOT_EXISTS_USER);
  }

  @Test
  void getByUsername_whenCustomerFound_thenReturnCustomerDto() {
    //given
    try (MockedStatic<CustomerMapper> customerMapperMock = mockStatic(CustomerMapper.class)) {
      var customer = Customer.builder().username(ADMIN).build();
      var customerDto = CustomerDto.builder().username(ADMIN).build();
      when(customerRepository.findByUsername(ADMIN)).thenReturn(Optional.of(customer));
      customerMapperMock.when(() -> CustomerMapper.mapToDto(Optional.of(customer)))
          .thenReturn(customerDto);
      //when
      var returnedCustomerDto = customerService.getByUsername(ADMIN);
      //then
      assertNotNull(returnedCustomerDto);
      assertEquals(returnedCustomerDto.id(), customer.getId());
      verify(customerRepository, times(1)).findByUsername(ADMIN);
    }
  }

  @Test
  void getByCustomerId_whenCustomerNotFound_thenReturnNoContent() {
    //given
    when(customerRepository.findById(NOT_EXISTS_CUSTOMER_ID)).thenReturn(Optional.empty());
    //when
    var customerDto = customerService.getByCustomerId(NOT_EXISTS_CUSTOMER_ID);
    //then
    assertNull(customerDto);
    verify(customerRepository, times(1)).findById(NOT_EXISTS_CUSTOMER_ID);
  }

  @Test
  void getByCustomerId_whenCustomerFound_thenReturnCustomerDto() {
    //given
    try (MockedStatic<CustomerMapper> customerMapperMock = mockStatic(CustomerMapper.class)) {
      var customer = Customer.builder().username(ADMIN).build();
      var customerDto = CustomerDto.builder().username(ADMIN).build();
      when(customerRepository.findById(ADMIN_ID)).thenReturn(Optional.of(customer));
      customerMapperMock.when(() -> CustomerMapper.mapToDto(Optional.of(customer)))
          .thenReturn(customerDto);
      //when
      var returnedCustomerDto = customerService.getByCustomerId(ADMIN_ID);
      //then
      assertNotNull(returnedCustomerDto);
      assertEquals(returnedCustomerDto.id(), customer.getId());
      verify(customerRepository, times(1)).findById(ADMIN_ID);
    }
  }
}