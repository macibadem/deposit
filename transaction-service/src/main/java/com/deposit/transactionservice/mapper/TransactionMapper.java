package com.deposit.transactionservice.mapper;


import com.deposit.transactionservice.dto.TransactionDto;
import com.deposit.transactionservice.entity.Transaction;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionMapper {

  public static TransactionDto mapToDto(Transaction transaction) {
    return TransactionDto.builder()
        .amount(transaction.getAmount())
        .accountId(transaction.getAccountId())
        .timestamp(transaction.getTimestamp())
        .build();
  }
}
