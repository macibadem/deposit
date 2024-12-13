package com.deposit.apigateway.security.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.deposit.apigateway.client.CustomerClient;
import com.deposit.apigateway.client.dto.CustomerDto;
import com.deposit.apigateway.constants.CommonConstants.Modules;
import com.deposit.apigateway.security.properties.ClientAuthProperties;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

  private static final Long ADMIN_CUSTOMER_ID = 1L;
  private static final String ADMIN_USERNAME = "admin";
  private static final String ADMIN_PASSWORD = "123456";
  private static final String API_GATEWAY_SECRET = "secret";
  @Mock
  private CustomerClient customerClient;
  @InjectMocks
  private UserDetailsService userDetailsService;
  @Mock
  private ClientAuthProperties clientAuthProperties;

  @BeforeEach
  void setUp() {
    ClientAuthProperties authProps = new ClientAuthProperties();
    authProps.setProperties(Map.of(Modules.API_GATEWAY, API_GATEWAY_SECRET));
    when(clientAuthProperties.getProperties()).thenReturn(authProps.getProperties());
  }

  @Test
  void loadUserByUsername_whenUserFound_thenReturnUserDetails() {
    //given
    var customerDto = CustomerDto.builder()
        .id(ADMIN_CUSTOMER_ID)
        .username(ADMIN_USERNAME)
        .password(ADMIN_PASSWORD)
        .build();
    when(customerClient.getCustomerById(customerDto.id(), Modules.API_GATEWAY,
        API_GATEWAY_SECRET)).thenReturn(customerDto);
    //when
    var userDetails = userDetailsService.loadUserByUsername(ADMIN_CUSTOMER_ID.toString());
    //then
    assertNotNull(userDetails);
    assertEquals(ADMIN_CUSTOMER_ID.toString(), userDetails.getUsername());
  }

  @Test
  void loadUserByUsername_UserNotFound() {
    //given
    when(customerClient.getCustomerById(anyLong(), anyString(), anyString())).thenReturn(
        null);
    //when
    var exception = assertThrows(UsernameNotFoundException.class,
        () -> userDetailsService.loadUserByUsername(ADMIN_CUSTOMER_ID.toString()));
    //then
    assertEquals("User not found", exception.getMessage());
  }
}