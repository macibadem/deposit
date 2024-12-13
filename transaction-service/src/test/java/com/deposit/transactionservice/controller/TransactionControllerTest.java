package com.deposit.transactionservice.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deposit.transactionservice.dto.TransactionDto;
import com.deposit.transactionservice.service.TransactionService;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

  @Mock
  private TransactionService transactionService;
  @InjectMocks
  private TransactionController transactionController;

  @Test
  void getTransactionsByAccountId_whenValidRequest_thenReturnTransactionPage() {
    //given
    var accountId = 1L;
    var pageable = PageRequest.of(0, 5);
    var transactionDto = new TransactionDto(1L, new BigDecimal("10.0"), Instant.now());
    var transactionPage = new PageImpl<>(List.of(transactionDto), pageable, 1);
    when(transactionService.getTransactionsByAccountId(accountId, pageable)).thenReturn(transactionPage);
    //when
    var response = transactionController.getTransactionsByAccountId(accountId, 0, 5);
    //then
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
    verify(transactionService).getTransactionsByAccountId(accountId, pageable);
  }
}