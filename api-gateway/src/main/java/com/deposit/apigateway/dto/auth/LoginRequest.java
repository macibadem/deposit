package com.deposit.apigateway.dto.auth;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LoginRequest (@NotNull String username, @NotNull String password){

}