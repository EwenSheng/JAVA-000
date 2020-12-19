package provider.dao.mapper;

import com.syw.rpc.example.api.model.USDFreezeAccount;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 16:15
 * @description:
 */
public interface RMBAccountMapper {

    @Select("SELECT * FROM rmb_account WHERE id =#{id} and user_id = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
    })
    USDFreezeAccount get(Long id);

    @Insert("INSERT INTO rmb_account(`user_id`, `balance`, `account_id`) VALUES(#{userId}, #{balance}, #{account_id})")
    void insert(int userId, BigDecimal balance);

    @Update("UPDATE rmb_account SET balance = #{balance} WHERE id = #{id} and user_id = #{userId}")
    int update(Long id, Long userId, BigDecimal balance);
}
