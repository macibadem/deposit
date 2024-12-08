package com.deposit.apigateway.dto.auth;

import lombok.Builder;

@Builder
public record LoginResponse(String token){

}