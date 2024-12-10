package com.deposit.customerservice.service;

import com.deposit.customerservice.dto.CustomerDto;
import com.deposit.customerservice.mapper.CustomerMapper;
import com.deposit.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;

  public CustomerDto getByUsername(String username) {
    return CustomerMapper.mapToDto(customerRepository.findByUsername(username));
  }

  public CustomerDto getByCustomerId(Long id) {
    return CustomerMapper.mapToDto(customerRepository.findById(id));
  }
}