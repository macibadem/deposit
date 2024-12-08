package com.deposit.customerservice.repository;

import com.deposit.customerservice.entity.Customer;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  Optional<Customer> findByUsername(String username);
}
