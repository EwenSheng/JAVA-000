package provider.service.impl;

import com.syw.rpc.example.api.model.USDAccount;
import com.syw.rpc.example.api.service.USDAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import provider.dao.mapper.USDAccountMapper;
import provider.dao.mapper.USDFreezeAccountMapper;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 10:53
 * @description:
 */
@Service("usdAccountService")
public class USDAccountServiceImpl implements USDAccountService {

    @Autowired
    private USDAccountMapper usdAccountMapper;

    @Autowired
    private USDFreezeAccountMapper usdFreezeAccountMapper;

    @Override
    public boolean transfer(USDAccount model) {
        return true;
    }
}
