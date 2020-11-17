package org.home.work.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/17 15:12
 * @description:
 */
@Configuration
public class OriginalJdbcConfiguration {

    /*
     * 加载驱动有两种方式
     *
     * 1：会导致驱动会注册两次，过度依赖于mysql的api，脱离的mysql的开发包，程序则无法编译
     * 2：驱动只会加载一次，不需要依赖具体的驱动，灵活性高
     *
     */
    /*@Bean
    public Connection connection() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");

        Connection connection = DriverManager.getConnection("jdbc:mysql://47.102.152.243:3306/syw_jdbc_demo", "root", "Dv8,zia13vvKqdly");

        return connection;
    }*/

    @Autowired
    private DataSource dataSource;

    @Bean
    public Connection connection() throws SQLException {

        /*HikariDataSource dataSource = new HikariDataSource();*/

        Connection connection = dataSource.getConnection();

        return connection;
    }


}