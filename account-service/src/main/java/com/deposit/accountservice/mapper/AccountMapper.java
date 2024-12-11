package com.deposit.accountservice.mapper;

import com.deposit.accountservice.dto.AccountDto;
import com.deposit.accountservice.entity.Account;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountMapper {

  public static List<AccountDto> map(final List<Account> accountList) {
    return accountList.stream().map(AccountMapper::mapToDto).collect(Collectors.toList());
  }

  private static AccountDto mapToDto(Account account) {
    return Optional.ofNullable(account).map(acc ->
            AccountDto.builder()
                .id(acc.getId())
                .balance(acc.getBalance())
                .customerId(acc.getCustomerId())
                .build())
        .orElse(null);
  }
}