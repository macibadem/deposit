package com.deposit.accountservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deposit.accountservice.constants.CommonConstants.Topics;
import com.deposit.accountservice.dto.AccountDto;
import com.deposit.accountservice.entity.Account;
import com.deposit.accountservice.kafka.event.AccountCreatedEvent;
import com.deposit.accountservice.mapper.AccountMapper;
import com.deposit.accountservice.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @InjectMocks
  private AccountService accountService;
  @Mock
  private AccountRepository accountRepository;
  @Mock
  private KafkaTemplate<String, AccountCreatedEvent> kafkaTemplate;

  @Test
  void createAccount_whenValidRequest_thenCreateAccountAndEvent() {
    //given
    var customerId = 1L;
    var initialCredit = BigDecimal.valueOf(10.0);
    var savedAccount = Account.builder()
        .id(1L)
        .customerId(customerId)
        .balance(initialCredit)
        .build();
    when(accountRepository.save(Mockito.any(Account.class))).thenReturn(savedAccount);
    var mockFuture = Mockito.mock(CompletableFuture.class);
    when(kafkaTemplate.send(eq(Topics.ACCOUNT_CREATED),
        Mockito.any(AccountCreatedEvent.class))).thenReturn(mockFuture);
    //when
    accountService.createAccount(customerId, initialCredit);
    //then
    verify(accountRepository).save(Mockito.any(Account.class));
    verify(kafkaTemplate).send(eq(Topics.ACCOUNT_CREATED), Mockito.any(AccountCreatedEvent.class));
  }

  @Test
  void getAccountByCustomerId_whenValidCustomerId_thenReturnsMappedAccounts() {
    //given
    Long customerId = 1L;
    var account = Account.builder()
        .id(1L)
        .customerId(customerId)
        .balance(BigDecimal.valueOf(10.0))
        .build();
    var accounts = List.of(account);
    var accountDto =new AccountDto(1L, customerId, BigDecimal.valueOf(10.0));
    var accountDtoList = List.of(accountDto);
    when(accountRepository.findAllByCustomerId(customerId)).thenReturn(accounts);
    try (MockedStatic<AccountMapper> mockedMapper = Mockito.mockStatic(AccountMapper.class)) {
      mockedMapper.when(() -> AccountMapper.map(accounts)).thenReturn(accountDtoList);
      //when
      var result = accountService.getAccountByCustomerId(customerId);
      //then
      assertNotNull(result);
      assertEquals(1, result.size());
      verify(accountRepository).findAllByCustomerId(customerId);
    }
  }
}
