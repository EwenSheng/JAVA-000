package com.syw.rpc.example.api.service;

import com.syw.rpc.example.api.model.USDAccount;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 10:52
 * @description:
 */
public interface USDAccountService {

    boolean transfer(USDAccount model);
}
