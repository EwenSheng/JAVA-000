package provider.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/9 10:04
 * @description:
 */
@Configuration
public class ShardingConfiguration {

    @Bean
    public SqlSessionTemplate sqlTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
