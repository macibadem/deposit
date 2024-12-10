package com.deposit.accountservice.dto;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record CreateAccountRequest(
    @NotNull(message = "initial credit must be positive.")
    @DecimalMin(value = "0.1", message = "initial credit must be positive.")
    @DecimalMax(value = "1000000000.0", message = "initial credit cannot exceed 1,000,000,000.")
    @Digits(integer = 10, fraction = 2, message = "initial credit must have up to 10 digits and 2 decimal places.")
    BigDecimal initialCredit){

}