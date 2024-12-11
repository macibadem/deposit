package com.deposit.accountservice.service;

import com.deposit.accountservice.constants.CommonConstants.Topics;
import com.deposit.accountservice.dto.AccountDto;
import com.deposit.accountservice.entity.Account;
import com.deposit.accountservice.kafka.event.AccountCreatedEvent;
import com.deposit.accountservice.mapper.AccountMapper;
import com.deposit.accountservice.repository.AccountRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final KafkaTemplate<String, AccountCreatedEvent> kafkaTemplate;

  public void createAccount(Long customerId, BigDecimal initialCredit) {
    var account = Account.builder()
        .customerId(customerId)
        .balance(initialCredit)
        .build();
    var savedAccount = accountRepository.save(account);
    var accountCreatedEvent = new AccountCreatedEvent(
        savedAccount.getId(),
        initialCredit,
        Instant.now(),
        UUID.randomUUID().toString());
    kafkaTemplate.send(Topics.ACCOUNT_CREATED, accountCreatedEvent);
  }

  public List<AccountDto> getAccountByCustomerId(Long customerId) {
    return AccountMapper.map(accountRepository.findAllByCustomerId(customerId));
  }
}