package com.deposit.queryservice.dto;

import com.deposit.queryservice.client.dto.TransactionDto;
import java.util.List;
import lombok.Builder;

@Builder
public record AccountInfoDto(Long accountId, List<TransactionDto> transactions) {

}