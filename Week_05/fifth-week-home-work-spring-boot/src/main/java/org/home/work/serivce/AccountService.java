package org.home.work.serivce;

import org.home.work.dao.AccountDao;
import org.home.work.entity.AccountDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/17 15:04
 * @description:
 */
@Service
public class AccountService {

    @Autowired
    private AccountDao accountDao;


    public AccountDO getAccountById(Integer id) throws SQLException {

        return accountDao.getAccountById(id);
    }

    public boolean updateAccountName(Integer id, String name) throws SQLException {

        return accountDao.updateAccountNameById(id, name);
    }

    public boolean updateBatchAccountByIds(List<Integer> ids) throws SQLException {
        return accountDao.updateBatchAccountByIds(ids);
    }
}
