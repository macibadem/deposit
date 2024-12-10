package com.deposit.transactionservice.dto;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;

@Builder
public record TransactionDto(Long accountId, BigDecimal amount, Instant timestamp) {

}
