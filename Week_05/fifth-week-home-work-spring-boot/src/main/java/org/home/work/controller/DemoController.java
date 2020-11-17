package org.home.work.controller;

import org.home.work.entity.vo.AccountBatchIdUpdateVO;
import org.home.work.entity.vo.AccountSingleUpdateVO;
import org.home.work.serivce.AccountService;
import org.home.work.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/16 20:17
 * @description:
 */
@RestController
@RequestMapping(value = "/demo")
public class DemoController {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private AccountService accountService;

    @GetMapping(value = "/print")
    public String print() {

        schoolService.doIt();

        return "success";
    }

    @GetMapping(value = "/get_account/{id}")
    public String getAccount(@PathVariable(name = "id") Integer id) throws SQLException {

        return accountService.getAccountById(id).toString();
    }

    @PostMapping(value = "/update_account")
    public String updateAccountName(@RequestBody AccountSingleUpdateVO vo) throws SQLException {

        return accountService.updateAccountName(vo.getId(), vo.getName()) ? "success" : "fail";
    }

    @PostMapping(value = "/update_batch_account")
    public String updateBatchAccountName(@RequestBody AccountBatchIdUpdateVO vo) throws SQLException {

        return accountService.updateBatchAccountByIds(vo.getIds()) ? "success" : "fail";
    }
}
