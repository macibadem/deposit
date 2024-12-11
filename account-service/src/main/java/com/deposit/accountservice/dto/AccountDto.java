package com.deposit.accountservice.dto;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record AccountDto(Long id, Long customerId, BigDecimal balance) {

}