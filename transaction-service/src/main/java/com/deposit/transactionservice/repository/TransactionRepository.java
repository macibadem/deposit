package com.deposit.transactionservice.repository;

import com.deposit.transactionservice.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  boolean existsByAccountIdAndIdentifier(Long accountId, String identifier);

  Page<Transaction> findByAccountIdOrderByTimestampDesc(Long accountId, Pageable pageable);
}