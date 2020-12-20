package provider.service.impl;

import com.syw.rpc.example.api.model.RMBAccount;
import com.syw.rpc.example.api.service.RMBAccountService;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import provider.dao.mapper.RMBAccountMapper;
import provider.dao.mapper.RMBFreezeAccountMapper;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 10:53
 * @description:
 */
@Service("rmbAccountService")
public class RMBAccountServiceImpl implements RMBAccountService {

    @Autowired
    private RMBAccountMapper rmbAccountMapper;

    @Autowired
    private RMBFreezeAccountMapper rmbFreezeAccountMapper;

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean transfer(RMBAccount model) {
        return true;
    }

    public Boolean confirm(RMBAccount model) {
        System.out.println("confirm =>>>>>" + model);
        return true;
    }

    public Boolean cancel(RMBAccount model) {
        System.out.println("cancel =>>>>>" + model);
        return true;
    }
}
