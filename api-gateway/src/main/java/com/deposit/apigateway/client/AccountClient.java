package com.deposit.apigateway.client;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "account-service", url = "${account.service.url}")
public interface AccountClient {

  @PostMapping(value = "/{path}", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> post(@PathVariable("path") String path,
      @RequestBody String body,
      @RequestHeader Map<String, String> headers);

}
