package consumer.serivce.impl;

import com.syw.rpc.example.api.model.dto.RMBAccountDTO;
import com.syw.rpc.example.api.model.dto.USDAccountDTO;
import com.syw.rpc.example.api.service.RMBAccountService;
import com.syw.rpc.example.api.service.USDAccountService;
import consumer.serivce.TransferService;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/20 12:52
 * @description:
 */
@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    private USDAccountService usdAccountService;

    @Autowired
    private RMBAccountService rmbAccountService;

    /**
     * @author: Ewen
     * @date: 2020/12/20 10:51
     * @param: [userId, balance]
     * @return: java.lang.Boolean
     * @description: 美元转人民币业务
     */
    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public Boolean transferToRMB(Long userId, BigDecimal balance) {

        //1. 美元账户扣款 TCC
        USDAccountDTO usdAccountDTO = new USDAccountDTO();
        usdAccountDTO.setUserId(userId);
        usdAccountDTO.setBalance(balance);
        usdAccountDTO.setDeduction(true);
        usdAccountService.operating(usdAccountDTO);

        //2. 人民币账户充值 TCC
        RMBAccountDTO rmbAccountDTO = new RMBAccountDTO();
        rmbAccountDTO.setUserId(userId);
        rmbAccountDTO.setBalance(balance.multiply(new BigDecimal(7)));
        rmbAccountDTO.setDeduction(false);
        rmbAccountService.operating(rmbAccountDTO);

        return true;
    }

    /**
     * @author: Ewen
     * @date: 2020/12/20 10:51
     * @param: [userId, balance]
     * @return: java.lang.Boolean
     * @description: 人民币转美元业务
     */
    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public Boolean transferToUsd(Long userId, BigDecimal balance) {

        //1. 人民币账户扣款 TCC
        RMBAccountDTO rmbAccountDTO = new RMBAccountDTO();
        rmbAccountDTO.setUserId(userId);
        rmbAccountDTO.setBalance(balance);
        rmbAccountDTO.setDeduction(true);
        rmbAccountService.operating(rmbAccountDTO);

        //2. 美元账户充值 TCC
        USDAccountDTO usdAccountDTO = new USDAccountDTO();
        usdAccountDTO.setUserId(userId);
        usdAccountDTO.setBalance(balance.divide(new BigDecimal(7)));
        usdAccountDTO.setDeduction(false);
        usdAccountService.operating(usdAccountDTO);

        return true;
    }

    public Boolean confirm(Long userId, BigDecimal balance) {
        System.out.println("confirm =>>>>> userid:" + userId + ",balance:" + balance);
        return true;
    }

    public Boolean cancel(Long userId, BigDecimal balance) {
        System.out.println("cancel =>>>>> userid:" + userId + ",balance:" + balance);
        return true;
    }

}
