package com.deposit.apigateway.client;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "query-service", url = "${query.service.url}")
public interface QueryClient {

  @GetMapping(value = "/{path}")
  ResponseEntity<String> get(@PathVariable("path") String path,
      @RequestHeader Map<String, String> headers);

}
