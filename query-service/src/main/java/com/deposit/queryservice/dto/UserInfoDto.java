package com.deposit.queryservice.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

@Builder
public record UserInfoDto(String name, String surname, BigDecimal balance, List<AccountInfoDto> accountInfoList) {

}