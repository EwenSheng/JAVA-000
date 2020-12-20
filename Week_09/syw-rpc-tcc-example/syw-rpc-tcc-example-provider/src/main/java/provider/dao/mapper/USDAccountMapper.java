package provider.dao.mapper;

import org.apache.ibatis.annotations.*;
import provider.dao.enity.USDAccount;

import java.math.BigDecimal;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 16:15
 * @description:
 */
public interface USDAccountMapper {

    @Select("SELECT id,user_id,balance FROM usd_account WHERE user_id = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "balance", column = "balance"),
    })
    USDAccount get(Long userId);

    @Insert("INSERT INTO usd_account(`user_id`, `balance`, `account_id`) VALUES(#{userId}, #{balance}, #{account_id})")
    void insert(int userId, BigDecimal balance);

    @Update("UPDATE usd_account SET balance = #{balance} WHERE user_id = #{userId}")
    int update(Long userId, BigDecimal balance);
}
