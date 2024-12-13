package com.deposit.queryservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deposit.queryservice.client.AccountClient;
import com.deposit.queryservice.client.CustomerClient;
import com.deposit.queryservice.client.TransactionClient;
import com.deposit.queryservice.client.dto.AccountDto;
import com.deposit.queryservice.client.dto.CustomerDto;
import com.deposit.queryservice.client.dto.TransactionDto;
import com.deposit.queryservice.constants.CommonConstants.Modules;
import com.deposit.queryservice.security.properties.ClientAuthProperties;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QueryServiceTest {

  @InjectMocks
  private QueryService queryService;
  @Mock
  private AccountClient accountClient;
  @Mock
  private CustomerClient customerClient;
  @Mock
  private TransactionClient transactionClient;
  @Mock
  private ClientAuthProperties clientAuthProperties;

  @Test
  void getUserInfo_whenValidCustomerId_thenReturnsUserInfo() {
    //given
    var customerId = 1L;
    var secret = "secret";
    when(clientAuthProperties.getProperties()).thenReturn(Map.of(Modules.QUERY_SERVICE, secret));
    var customer = new CustomerDto(1L, "Mustafa", "Acibadem", "macibadem", "");
    when(customerClient.getCustomerById(customerId, Modules.QUERY_SERVICE, secret)).thenReturn(
        customer);
    var accounts = List.of(
        new AccountDto(1L, 1L, BigDecimal.valueOf(100.0)),
        new AccountDto(2L, 1L, BigDecimal.valueOf(150.0))
    );
    when(accountClient.getAccountsByCustomerId(customerId, Modules.QUERY_SERVICE,
        secret)).thenReturn(accounts);
    var account1Transactions = List.of(
        new TransactionDto(1L, BigDecimal.valueOf(50.0), Instant.now()),
        new TransactionDto(1L, BigDecimal.valueOf(50.0), Instant.now())
    );
    List<TransactionDto> account2Transactions = List.of(
        new TransactionDto(2L, BigDecimal.valueOf(150.0), Instant.now())
    );
    when(transactionClient.getTransactionsByAccountId(1L, Modules.QUERY_SERVICE, secret))
        .thenReturn(account1Transactions);
    when(transactionClient.getTransactionsByAccountId(2L, Modules.QUERY_SERVICE, secret))
        .thenReturn(account2Transactions);
    //when
    var result = queryService.getUserInfo(customerId);
    //then
    assertNotNull(result);
    assertEquals(BigDecimal.valueOf(250.0), result.balance());
    assertEquals(2, result.accountInfoList().size());
    verify(clientAuthProperties).getProperties();
    verify(customerClient).getCustomerById(customerId, Modules.QUERY_SERVICE, secret);
    verify(accountClient).getAccountsByCustomerId(customerId, Modules.QUERY_SERVICE, secret);
    verify(transactionClient).getTransactionsByAccountId(1L, Modules.QUERY_SERVICE, secret);
    verify(transactionClient).getTransactionsByAccountId(2L, Modules.QUERY_SERVICE, secret);
  }
}
