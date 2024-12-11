package com.deposit.queryservice.client;

import com.deposit.queryservice.client.dto.TransactionDto;
import com.deposit.queryservice.constants.CommonConstants.Header;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "transaction-service", url = "${transaction.service.url}")
public interface TransactionClient {

  @GetMapping("/v0/transactions/by-accountId/{accountId}")
  List<TransactionDto> getTransactionsByAccountId(@PathVariable("accountId") Long accountId,
      @RequestHeader(Header.API_KEY) String apiKey,
      @RequestHeader(Header.API_SECRET) String apiSecret);
}
