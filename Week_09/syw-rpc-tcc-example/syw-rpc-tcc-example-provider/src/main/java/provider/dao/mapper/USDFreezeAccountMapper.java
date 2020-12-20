package provider.dao.mapper;

import org.apache.ibatis.annotations.*;
import provider.dao.enity.USDFreezeAccount;

import java.math.BigDecimal;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 16:16
 * @description:
 */
public interface USDFreezeAccountMapper {

    @Select("SELECT * FROM usd_freeze_account WHERE user_id = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "balance", column = "balance"),
    })
    USDFreezeAccount get(Long userId);

    @Insert("INSERT INTO usd_freeze_account(`user_id`, `balance`, `account_id`) VALUES(#{userId}, #{balance}, #{accountId})")
    void insert(Long userId, BigDecimal balance, Long accountId);

    @Update("UPDATE usd_freeze_account SET balance = #{balance} WHERE user_id = #{userId}")
    int update(Long userId, BigDecimal balance);

    @Delete("DELETE FROM usd_freeze_account WHERE user_id = #{userId}")
    int delete(Long userId);
}
