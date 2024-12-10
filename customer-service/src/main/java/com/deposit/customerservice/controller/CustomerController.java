package com.deposit.customerservice.controller;

import com.deposit.customerservice.dto.CustomerDto;
import com.deposit.customerservice.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v0/customers")
@Tag(name = "Customer Api", description = "Customer Api")
public class CustomerController {

  private final CustomerService customerService;

  @GetMapping("/by-username/{username}")
  @Operation(method = "GET", summary = "Customer By Username Service", description = "Get Customer By Username.")
  public ResponseEntity<CustomerDto> getByUsername(@NotNull @PathVariable("username") String username) {
    var customerDto = customerService.getByUsername(username);

    return Objects.isNull(customerDto) ?
        ResponseEntity.noContent().build() :
        ResponseEntity.ok().body(customerDto);
  }

  @GetMapping()
  @Operation(method = "GET", summary = "Customer By Id Service", description = "Get Customer By Id.")
  public ResponseEntity<CustomerDto> getByCustomerId(@NotNull @RequestParam(name = "id") Long id) {
    var customerDto = customerService.getByCustomerId(id);

    return Objects.isNull(customerDto) ?
        ResponseEntity.noContent().build() :
        ResponseEntity.ok().body(customerDto);
  }
}