package com.deposit.apigateway.client.dto;

import lombok.Builder;

@Builder
public record CustomerDto(Long id, String name, String surname, String username, String password) {

}
