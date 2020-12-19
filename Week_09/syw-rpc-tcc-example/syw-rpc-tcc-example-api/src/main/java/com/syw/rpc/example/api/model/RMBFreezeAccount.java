package com.syw.rpc.example.api.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 15:36
 * @description:
 */
@Data
public class RMBFreezeAccount {

    private Long id;

    private Long userId;

    private Long accountId;

    private BigDecimal balance;
}
