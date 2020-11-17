package org.home.work.dao;

import org.home.work.entity.AccountDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/17 15:05
 * @description:
 */
@Component
public class AccountDao {

    @Autowired
    private Connection connection;

    private Statement statement;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    public AccountDO getAccountById(Integer id) throws SQLException {

        AccountDO ado = new AccountDO();

        try {
            //获取执行sql语句的statement对象
            statement = connection.createStatement();

            //执行sql语句,拿到结果集
            resultSet = statement.executeQuery("SELECT * FROM account WHERE id = " + id);

            while (resultSet.next()) {

                ado = new AccountDO();
                ado.setAccountName(resultSet.getString("account_name"));
                ado.setPassword(resultSet.getString("password"));
                ado.setIdType(resultSet.getString("id_type"));
                ado.setIdNo(resultSet.getString("id_no"));
                ado.setMobile(resultSet.getString("mobile"));
                ado.setEmail(resultSet.getString("email"));
                ado.setRemark(resultSet.getString("remark"));

                /*System.out.println(resultSet.getString("account_name"));
                System.out.println(resultSet.getString("password"));
                System.out.println(resultSet.getString("id_type"));
                System.out.println(resultSet.getString("id_no"));
                System.out.println(resultSet.getString("mobile"));
                System.out.println(resultSet.getString("email"));
                System.out.println(resultSet.getString("remark"));
                System.out.println(resultSet.getString("last_login_dt"));
                System.out.println(resultSet.getString("last_login_ip"));
                System.out.println(resultSet.getString("sha"));
                System.out.println(resultSet.getString("sso_token"));
                System.out.println(resultSet.getString("is_valid"));
                System.out.println(resultSet.getString("is_deleted"));
                System.out.println(resultSet.getString("create_by"));
                System.out.println(resultSet.getString("create_dt"));
                System.out.println(resultSet.getString("update_by"));
                System.out.println(resultSet.getString("update_dt"));
                System.out.println(resultSet.getString("version"));*/
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }

            if (resultSet != null) {
                resultSet.close();
            }
        }

        return ado;
    }


    public boolean updateAccountNameById(Integer id, String name) throws SQLException {

        try {
            statement = connection.createStatement();

            // 执行DML（insert、update、delete）语句、DDL(create，alter、drop)语句 返回值：影响的行数，可以通过这个影响的行数判断DML语句是否执行成功 返回值>0的则执行成功，反之，则失败
            return statement.executeUpdate("UPDATE account SET account_name = '" + name + "' , version = version + 1 WHERE id = " + id) > 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }

            if (resultSet != null) {
                resultSet.close();
            }
        }
        return false;
    }

    public boolean updateBatchAccountByIds(List<Integer> ids) throws SQLException {

        connection.setAutoCommit(false);

        try {

            String sql = String.format(
                    "UPDATE account SET version = version + 1,update_by = 'local_dev',update_dt = NOW() WHERE id in (%s)",
                    ids.stream().map(String::valueOf).collect(Collectors.joining(","))
            );

            preparedStatement = connection.prepareStatement(sql);

            connection.commit();

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException throwables) {
            connection.rollback();
            throwables.printStackTrace();
        } finally {
            if (statement != null) {
                preparedStatement.close();
            }

            if (resultSet != null) {
                resultSet.close();
            }
        }
        return false;
    }
}
