package com.deposit.queryservice.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deposit.queryservice.client.dto.TransactionDto;
import com.deposit.queryservice.dto.AccountInfoDto;
import com.deposit.queryservice.dto.UserInfoDto;
import com.deposit.queryservice.service.QueryService;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class QueryControllerTest {

  @Mock
  private QueryService queryService;
  @InjectMocks
  private QueryController queryController;


  @Test
  void getUserInfo_whenValidCustomerIdGiven_thenReturnUserInfo() {
    //given
    Long customerId = 1L;
    var userInfoDto = createUserInfoDto();
    when(queryService.getUserInfo(customerId)).thenReturn(userInfoDto);
    //when
    var response = queryController.getUserInfo(customerId);
    //then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(queryService).getUserInfo(customerId);
  }

  private UserInfoDto createUserInfoDto() {
    return UserInfoDto.builder()
        .name("Mustafa")
        .surname("Acibadem")
        .balance(BigDecimal.valueOf(225.00))
        .accountInfoList(List.of(
            AccountInfoDto.builder()
                .accountId(1L)
                .transactions(List.of(
                    new TransactionDto(1L, BigDecimal.valueOf(75.50), Instant.now()),
                    new TransactionDto(1L, BigDecimal.valueOf(24.50), Instant.now())
                ))
                .build(),
            AccountInfoDto.builder()
                .accountId(2L)
                .transactions(List.of(
                    new TransactionDto(2L, BigDecimal.valueOf(125.00), Instant.now())
                ))
                .build()
        ))
        .build();
  }
}