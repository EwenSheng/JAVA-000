package com.syw.ss.xa.example.dao.mapper;

import com.syw.ss.xa.example.dao.model.Order;
import org.apache.ibatis.annotations.*;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/9 10:47
 * @description:
 */
public interface OrderMapper {

    @Select({"<script>",
            "SELECT * FROM order ",
            "<where>",
            "id = #{id}",
            "<if test='userId!=null'>",
            "user_id = #{userId}",
            "</if>",
            "</where>",
            "</script>"})
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
    })
    Order getByParams(@Param("id") Long id, @Param("user_id") Long userId);

    @Insert("INSERT INTO t_order(`user_id`, `status`) VALUES(#{userId}, #{stauts})")
    void insert(@Param("userId") Long userId, @Param("stauts") Integer stauts);

    @Delete("DELETE FROM order WHERE id = #{id}")
    int delete(@Param("id") Long id);

    @Update("UPDATE order SET status = #{stauts} WHERE id = #{id}")
    int update(@Param("id") Long id, @Param("stauts") Integer stauts);
}
