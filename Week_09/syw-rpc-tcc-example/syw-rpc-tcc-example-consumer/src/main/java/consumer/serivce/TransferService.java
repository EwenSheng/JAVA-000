package consumer.serivce;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/20 10:47
 * @description:
 */
@Service
public interface TransferService {

    public Boolean transferToRMB(Long userId, BigDecimal balance);

    public Boolean transferToUsd(Long userId, BigDecimal balance);
}
