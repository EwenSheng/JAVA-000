package provider.service.impl;

import com.syw.rpc.example.api.model.dto.RMBAccountDTO;
import com.syw.rpc.example.api.service.RMBAccountService;
import org.dromara.hmily.annotation.HmilyTCC;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import provider.dao.enity.RMBAccount;
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
    public Boolean operating(RMBAccountDTO dto) {

        System.out.println("RMBAccountDTO operating =>>>>>" + dto);

        RMBAccount account = rmbAccountMapper.get(dto.getUserId());

        if (dto.getDeduction()) {
            // 扣款行为
            if (dto.getBalance().compareTo(account.getBalance()) > 0) {
                throw new HmilyRuntimeException("余额不足");
            }
        }

        rmbFreezeAccountMapper.insert(dto.getUserId(), dto.getBalance(), account.getId());
        return true;
    }

    public Boolean confirm(RMBAccountDTO dto) {
        System.out.println("RMBAccountDTO confirm =>>>>>" + dto);
        rmbFreezeAccountMapper.delete(dto.getUserId());

        RMBAccount account = rmbAccountMapper.get(dto.getUserId());
        account.setBalance(dto.getDeduction() ? account.getBalance().subtract(dto.getBalance()) : account.getBalance().add(dto.getBalance()));

        rmbAccountMapper.update(account.getUserId(), account.getBalance());
        return true;
    }

    public Boolean cancel(RMBAccountDTO dto) {
        System.out.println("RMBAccountDTO cancel =>>>>>" + dto);
        rmbFreezeAccountMapper.delete(dto.getUserId());
        return true;
    }
}
