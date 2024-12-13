package com.deposit.apigateway.controller;


import com.deposit.apigateway.dto.auth.LoginRequest;
import com.deposit.apigateway.dto.auth.LoginResponse;
import com.deposit.apigateway.exception.GatewayException;
import com.deposit.apigateway.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v0/auth")
@Tag(name = "Authentication Api", description = "Authentication Api")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  @Operation(method = "POST", summary = "Login", description = "Login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody final LoginRequest request)
      throws GatewayException {
    return ResponseEntity.ok().body(authenticationService.login(request));
  }
}
