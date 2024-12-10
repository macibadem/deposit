package com.deposit.transactionservice.service;

import com.deposit.transactionservice.dto.TransactionDto;
import com.deposit.transactionservice.entity.Transaction;
import com.deposit.transactionservice.kafka.event.AccountCreatedEvent;
import com.deposit.transactionservice.mapper.TransactionMapper;
import com.deposit.transactionservice.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;

  public void createTransaction(AccountCreatedEvent event) {
    if (isValidTransaction(event)) {
      var transaction = Transaction.builder()
          .amount(event.initialCredit())
          .timestamp(event.timestamp())
          .accountId(event.accountId())
          .identifier(event.identifier())
          .build();
      transactionRepository.save(transaction);
    }
  }

  public Page<TransactionDto> getTransactionsByAccountId(Long accountId, Pageable pageable) {
    var transactions = transactionRepository.findByAccountIdOrderByTimestampDesc(accountId, pageable);
    return  transactions.map(TransactionMapper::mapToDto);
  }

  private boolean isValidTransaction(AccountCreatedEvent event) {
    return Objects.nonNull(event.initialCredit()) &&
        BigDecimal.ZERO.compareTo(event.initialCredit()) < 0 &&
        !transactionRepository.existsByAccountIdAndIdentifier(event.accountId(),
            event.identifier());
  }
}
