package com.syw.rpc.example.api.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 15:36
 * @description:
 */
@Data
public class RMBAccountDTO implements Serializable {

    private Long userId;

    private BigDecimal balance;

    private Boolean deduction;
}
