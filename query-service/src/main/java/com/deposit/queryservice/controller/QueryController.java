package com.deposit.queryservice.controller;

import com.deposit.queryservice.constants.CommonConstants.Header;
import com.deposit.queryservice.dto.UserInfoDto;
import com.deposit.queryservice.service.QueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v0/queries")
@Tag(name = "Query Api", description = "Query Api")
public class QueryController {

  private final QueryService queryService;


  @GetMapping("/user-info")
  @Operation(method = "GET", summary = "Get User Info Service", description = "Get User Info Service.")
  public ResponseEntity<UserInfoDto> getUserInfo(
      @NotNull @RequestHeader(Header.CUSTOMER_ID) Long customerId) {
    return ResponseEntity.ok(queryService.getUserInfo(customerId));
  }
}