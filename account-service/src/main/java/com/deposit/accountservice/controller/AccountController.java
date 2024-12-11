package com.deposit.accountservice.controller;

import com.deposit.accountservice.constants.CommonConstants.Header;
import com.deposit.accountservice.dto.AccountDto;
import com.deposit.accountservice.dto.CreateAccountRequest;
import com.deposit.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v0/accounts")
@Tag(name = "Account Api", description = "Account Api")
public class AccountController {

  private final AccountService accountService;

  @PostMapping
  @Operation(method = "POST", summary = "Create Account Service", description = "Create Account Service.")
  public ResponseEntity<Void> createAccount(
      @NotNull @RequestHeader(Header.CUSTOMER_ID) Long customerId,
      @Valid @RequestBody CreateAccountRequest request) {
    accountService.createAccount(customerId, request.initialCredit());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/by-customerId/{customerId}")
  @Operation(method = "GET", summary = "Get Account by Customer ID Service", description = "Get Account by Customer ID Service.")
  public ResponseEntity<List<AccountDto>> getAccountByCustomerId(
      @NotNull @PathVariable Long customerId) {
    return ResponseEntity.ok(accountService.getAccountByCustomerId(customerId));
  }
}