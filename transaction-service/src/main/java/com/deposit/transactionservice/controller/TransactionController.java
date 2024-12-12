package com.deposit.transactionservice.controller;

import com.deposit.transactionservice.dto.TransactionDto;
import com.deposit.transactionservice.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v0/transactions")
@Tag(name = "Transaction Api", description = "Transaction Api")
public class TransactionController {

  private final TransactionService transactionService;


  @GetMapping("/by-accountId/{accountId}")
  @Operation(method = "GET", summary = "Get Transactions by Account ID Service", description = "Get Transactions by Account ID Service.")
  public ResponseEntity<List<TransactionDto>> getTransactionsByAccountId(
      @NotNull @PathVariable Long accountId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size) {

    var pageable = PageRequest.of(page, size);
    var transactions = transactionService.getTransactionsByAccountId(accountId, pageable);
    var transactionList = !transactions.isEmpty() ? transactions.getContent() : null;
    return ResponseEntity.ok(transactionList);
  }
}