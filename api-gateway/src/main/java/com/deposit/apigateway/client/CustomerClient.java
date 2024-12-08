package com.deposit.apigateway.client;

import com.deposit.apigateway.constants.CommonConstants.Header;
import com.deposit.apigateway.client.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "customer-service", url = "${customer.service.url}")
public interface CustomerClient {
  @GetMapping("/v0/customers/by-username/{username}")
  CustomerDto getCustomerByUsername(@PathVariable("username") String username,
      @RequestHeader(Header.API_KEY) String apiKey,
      @RequestHeader(Header.API_SECRET) String apiSecret);
}
