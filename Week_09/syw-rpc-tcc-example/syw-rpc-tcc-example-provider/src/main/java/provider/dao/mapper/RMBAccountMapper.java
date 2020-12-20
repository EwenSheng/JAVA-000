package provider.dao.mapper;

import org.apache.ibatis.annotations.*;
import provider.dao.enity.RMBAccount;

import java.math.BigDecimal;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 16:15
 * @description:
 */
public interface RMBAccountMapper {

    @Select("SELECT id,user_id,balance FROM rmb_account WHERE user_id = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "balance", column = "balance"),
    })
    RMBAccount get(Long userId);

    @Insert("INSERT INTO rmb_account(`user_id`, `balance`, `account_id`) VALUES(#{userId}, #{balance}, #{account_id})")
    void insert(Long userId, BigDecimal balance);

    @Update("UPDATE rmb_account SET balance = #{balance} WHERE user_id = #{userId}")
    int update(Long userId, BigDecimal balance);
}
