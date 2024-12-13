package com.deposit.queryservice.client;

import com.deposit.queryservice.client.dto.CustomerDto;
import com.deposit.queryservice.constants.CommonConstants.Header;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "customer-service", url = "${customer.service.url}")
public interface CustomerClient {
  @GetMapping("/v0/customers")
  CustomerDto getCustomerById(@RequestParam("id") Long customerId,
      @RequestHeader(Header.API_KEY) String apiKey,
      @RequestHeader(Header.API_SECRET) String apiSecret);
}
