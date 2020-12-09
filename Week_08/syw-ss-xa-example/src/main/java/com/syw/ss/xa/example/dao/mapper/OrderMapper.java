package com.syw.ss.xa.example.dao.mapper;

import com.syw.ss.xa.example.dao.model.Order;
import org.apache.ibatis.annotations.*;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/9 10:47
 * @description:
 */
public interface OrderMapper {

    @Select("SELECT * FROM t_order WHERE id =#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
    })
    Order get(Long id);

    @Insert("INSERT INTO t_order(`user_id`, `status`) VALUES(#{userId}, #{stauts})")
    void insert(int userId, Integer stauts);

    @Delete("DELETE FROM t_order WHERE id = #{id}")
    int delete(Long id);

    @Update("UPDATE t_order SET status = #{stauts} WHERE id = #{id}")
    int update(Long id, Integer stauts);
}
