package com.deposit.transactionservice.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deposit.transactionservice.entity.Transaction;
import com.deposit.transactionservice.kafka.event.AccountCreatedEvent;
import com.deposit.transactionservice.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
  @InjectMocks
  private TransactionService transactionService;
  @Mock
  private TransactionRepository transactionRepository;

  @Test
  void createTransaction_whenValidEventSent_thenCreateTransaction() {
    //given
    var event = new AccountCreatedEvent(
        1L,
        BigDecimal.valueOf(10.0),
        Instant.now(),
        "identifier"
    );
    when(transactionRepository.existsByAccountIdAndIdentifier(event.accountId(), event.identifier()))
        .thenReturn(false);
    //when
    transactionService.createTransaction(event);
    //then
    verify(transactionRepository).save(any());
  }

  @Test
  void createTransaction_whenTransactionAlreadyCreated_thenDontCreateTransaction() {
    //given
    var event = new AccountCreatedEvent(
        1L,
        BigDecimal.valueOf(10.0),
        Instant.now(),
        "identifier"
    );
    when(transactionRepository.existsByAccountIdAndIdentifier(event.accountId(), event.identifier()))
        .thenReturn(true);
    //when
    transactionService.createTransaction(event);
    //then
    verify(transactionRepository, never()).save(any(Transaction.class));
  }

  @Test
  void getTransactionsByAccountId_whenTransactionExists_thenReturnPage() {
    //given
    var accountId = 1L;
    var pageable = PageRequest.of(0, 5);
    var transaction = Transaction.builder()
        .id(1L)
        .accountId(accountId)
        .amount(BigDecimal.valueOf(10.0))
        .timestamp(Instant.now())
        .identifier("identifier")
        .build();
    var transactionPage = new PageImpl<>(List.of(transaction), pageable, 1);
    when(transactionRepository.findByAccountIdOrderByTimestampDesc(accountId, pageable))
        .thenReturn(transactionPage);
    //when
    var result = transactionService.getTransactionsByAccountId(accountId, pageable);
    //then
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
  }

  @Test
  void getTransactionsByAccountId_whenNoTransaction_thenReturnEmptyPage() {
    //given
    var accountId = 1L;
    var pageable = PageRequest.of(0, 5);
    when(transactionRepository.findByAccountIdOrderByTimestampDesc(accountId, pageable))
        .thenReturn(Page.empty(pageable));
    //when
    var result = transactionService.getTransactionsByAccountId(accountId, pageable);
    //then
    assertNotNull(result);
    assertEquals(0, result.getTotalElements());
  }
}