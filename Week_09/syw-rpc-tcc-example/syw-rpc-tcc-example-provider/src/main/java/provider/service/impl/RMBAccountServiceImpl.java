package provider.service.impl;

import com.syw.rpc.example.api.model.RMBAccount;
import com.syw.rpc.example.api.service.RMBAccountService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import provider.dao.mapper.RMBAccountMapper;
import provider.dao.mapper.RMBFreezeAccountMapper;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 10:53
 * @description:
 */
@Service
public class RMBAccountServiceImpl implements RMBAccountService {

    @Autowired
    private RMBAccountMapper rmbAccountMapper;

    @Autowired
    private RMBFreezeAccountMapper rmbFreezeAccountMapper;


    @Override
    public boolean transfer(RMBAccount model) {
        return true;
    }
}
