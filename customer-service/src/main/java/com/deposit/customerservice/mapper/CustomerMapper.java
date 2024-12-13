package com.deposit.customerservice.mapper;


import com.deposit.customerservice.dto.CustomerDto;
import com.deposit.customerservice.entity.Customer;
import java.util.Optional;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomerMapper {

  public static CustomerDto mapToDto(final Optional<Customer> customerOptional) {
    return customerOptional.map(customer ->
            CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .username(customer.getUsername())
                .password(customer.getPassword())
                .build())
        .orElse(null);
  }
}
