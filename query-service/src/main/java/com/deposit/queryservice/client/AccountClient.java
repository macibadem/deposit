package com.deposit.queryservice.client;

import com.deposit.queryservice.client.dto.AccountDto;
import com.deposit.queryservice.constants.CommonConstants.Header;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "account-service", url = "${account.service.url}")
public interface AccountClient {

  @GetMapping("/v0/accounts/by-customerId/{customerId}")
  List<AccountDto> getAccountsByCustomerId(@PathVariable("customerId") Long customerId,
      @RequestHeader(Header.API_KEY) String apiKey,
      @RequestHeader(Header.API_SECRET) String apiSecret);
}
