package com.deposit.transactionservice.kafka.event;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountCreatedEvent(Long accountId, BigDecimal initialCredit, Instant timestamp, String identifier) {
}
