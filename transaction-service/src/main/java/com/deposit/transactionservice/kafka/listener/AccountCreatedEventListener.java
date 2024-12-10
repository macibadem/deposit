package com.deposit.transactionservice.kafka.listener;


import com.deposit.transactionservice.constants.CommonConstants.Topics;
import com.deposit.transactionservice.kafka.config.KafkaConsumerConfig;
import com.deposit.transactionservice.kafka.event.AccountCreatedEvent;
import com.deposit.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountCreatedEventListener {

  private final TransactionService transactionService;

  @KafkaListener(topics = Topics.ACCOUNT_CREATED,
      groupId = KafkaConsumerConfig.TRANSACTION_SERVICE_GROUP,
      containerFactory = KafkaConsumerConfig.LISTENER_CONTAINER_FACTORY)
  public void consumeAccountCreatedEvent(AccountCreatedEvent event, Acknowledgment acknowledgment) {
    try {
      transactionService.createTransaction(event);
      acknowledgment.acknowledge();
    } catch (Exception e) {
      log.error("exception occurred while processing account-created event: ", e);
    }
  }
}
