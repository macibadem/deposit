package com.deposit.apigateway.controller;


import com.deposit.apigateway.client.AccountClient;
import com.deposit.apigateway.security.properties.ClientAuthProperties;
import com.deposit.apigateway.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/gateway")
public class GatewayController {

  private final AccountClient accountClient;
  private final ClientAuthProperties clientAuthProperties;


  @RequestMapping(value = "/account/**", method = {RequestMethod.POST})
  public ResponseEntity<?> handleAccountRequests(HttpServletRequest request,
      @Valid @RequestBody(required = false) String body) {
    try {
      var accountPath = HttpUtil.replaceGatewayAccountPath(request);
      var headerMap = HttpUtil.appendCustomHeaders(request, clientAuthProperties);

      if (RequestMethod.POST.equals(RequestMethod.resolve(request.getMethod()))) {
        return accountClient.post(accountPath, body, headerMap);
      }
      return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Method not allowed");
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("Can't forward request: " + e.getMessage());
    }
  }
}
