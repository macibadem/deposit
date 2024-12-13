package com.deposit.customerservice.dto;

import lombok.Builder;

@Builder
public record CustomerDto(Long id, String name, String surname, String username, String password) {

}
