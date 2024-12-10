package com.deposit.transactionservice.kafka.listener;


import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.deposit.transactionservice.kafka.event.AccountCreatedEvent;
import com.deposit.transactionservice.service.TransactionService;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

@ExtendWith(MockitoExtension.class)
class AccountCreatedEventListenerTest {

  @Mock
  private Acknowledgment acknowledgment;
  @Mock
  private TransactionService transactionService;
  @InjectMocks
  private AccountCreatedEventListener eventListener;


  @Test
  void consumeAccountCreatedEvent_whenValidEventConsumed_thenCreateTransaction() {
    //given
    var event = createdEvent();
    //given
    eventListener.consumeAccountCreatedEvent(event, acknowledgment);
    //then
    verify(acknowledgment).acknowledge();
    verify(transactionService).createTransaction(event);
  }

  @Test
  void consumeAccountCreatedEvent_whenExceptionOccurs_thenDontAcknowledge() {
    //given
    var event = createdEvent();
    doThrow(new RuntimeException("")).when(transactionService).createTransaction(event);
    //when
    eventListener.consumeAccountCreatedEvent(event, acknowledgment);
    //then
    verify(acknowledgment, never()).acknowledge();
    verify(transactionService).createTransaction(event);
  }

  private AccountCreatedEvent createdEvent() {
    return new AccountCreatedEvent(
        1L,
        BigDecimal.valueOf(10.0),
        Instant.now(),
        "identifier"
    );
  }
}