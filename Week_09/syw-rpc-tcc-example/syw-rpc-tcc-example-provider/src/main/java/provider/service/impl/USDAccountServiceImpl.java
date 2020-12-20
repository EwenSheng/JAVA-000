package provider.service.impl;

import com.syw.rpc.example.api.model.dto.USDAccountDTO;
import com.syw.rpc.example.api.service.USDAccountService;
import org.dromara.hmily.annotation.HmilyTCC;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import provider.dao.enity.USDAccount;
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
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public Boolean operating(USDAccountDTO dto) {

        System.out.println("USDAccountDTO operating =>>>>>" + dto);

        USDAccount account = usdAccountMapper.get(dto.getUserId());

        if (dto.getDeduction()) {
            // 扣款行为
            if (dto.getBalance().compareTo(account.getBalance()) > 0) {
                throw new HmilyRuntimeException("余额不足");
            }
        }

        usdFreezeAccountMapper.insert(dto.getUserId(), dto.getBalance(), account.getId());
        return true;
    }

    public Boolean confirm(USDAccountDTO dto) {
        System.out.println("USDAccountDTO confirm =>>>>>" + dto);
        usdFreezeAccountMapper.delete(dto.getUserId());

        USDAccount account = usdAccountMapper.get(dto.getUserId());
        account.setBalance(dto.getDeduction() ? account.getBalance().subtract(dto.getBalance()) : account.getBalance().add(dto.getBalance()));

        usdAccountMapper.update(account.getUserId(), account.getBalance());
        return true;
    }

    public Boolean cancel(USDAccountDTO dto) {
        System.out.println("USDAccountDTO cancel =>>>>>" + dto);
        usdFreezeAccountMapper.delete(dto.getUserId());
        return true;
    }
}
