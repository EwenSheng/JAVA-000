package provider.dao.enity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 15:36
 * @description:
 */
@Data
public class USDAccount {

    private Long id;

    private Long userId;

    private BigDecimal balance;
}
