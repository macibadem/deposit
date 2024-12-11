package com.deposit.queryservice.service;

import com.deposit.queryservice.client.AccountClient;
import com.deposit.queryservice.client.CustomerClient;
import com.deposit.queryservice.client.TransactionClient;
import com.deposit.queryservice.client.dto.AccountDto;
import com.deposit.queryservice.client.dto.CustomerDto;
import com.deposit.queryservice.client.dto.TransactionDto;
import com.deposit.queryservice.constants.CommonConstants.Modules;
import com.deposit.queryservice.dto.AccountInfoDto;
import com.deposit.queryservice.dto.UserInfoDto;
import com.deposit.queryservice.security.properties.ClientAuthProperties;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class QueryService {

  private final AccountClient accountClient;
  private final CustomerClient customerClient;
  private final TransactionClient transactionClient;
  private final ClientAuthProperties clientAuthProperties;

  public UserInfoDto getUserInfo(Long customerId) {
    var secret = clientAuthProperties.getProperties().get(Modules.QUERY_SERVICE);

    return getCustomer(customerId, secret)
        .map(customer -> {
              var accountInfoList = new ArrayList<AccountInfoDto>();
              var totalBalance = getAccountList(customerId, secret)
                  .map(accounts -> accounts.stream()
                      .map(account -> {
                            var transactions = getTransactionList(account.id(), secret);
                            accountInfoList.add(new AccountInfoDto(account.id(), transactions));
                            return account.balance();
                          }
                      ).reduce(BigDecimal.ZERO, BigDecimal::add)
                  ).orElse(BigDecimal.ZERO);

              return UserInfoDto.builder()
                  .name(customer.name())
                  .surname(customer.surname())
                  .balance(totalBalance)
                  .accountInfoList(accountInfoList)
                  .build();
            }
        ).orElse(null);
  }

  private Optional<CustomerDto> getCustomer(Long customerId, String secret) {
    return Optional.ofNullable(
        customerClient.getCustomerById(customerId, Modules.QUERY_SERVICE, secret)
    );
  }

  private Optional<List<AccountDto>> getAccountList(Long customerId, String secret) {
    return Optional.ofNullable(
        accountClient.getAccountsByCustomerId(customerId, Modules.QUERY_SERVICE, secret)
    );
  }

  private List<TransactionDto> getTransactionList(Long accountId, String secret) {
    return Optional.ofNullable(
        transactionClient.getTransactionsByAccountId(accountId, Modules.QUERY_SERVICE, secret)
    ).orElse(Collections.emptyList());
  }
}