package com.syw.ss.xa.example.dao.model;

import lombok.Data;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/9 10:36
 * @description:
 */
@Data
public class Order {

    private Long id;
    private Long userId;
    private Integer status;
}
