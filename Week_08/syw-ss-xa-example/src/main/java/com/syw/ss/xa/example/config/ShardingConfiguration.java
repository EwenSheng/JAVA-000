package com.syw.ss.xa.example.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/9 10:04
 * @description: 参照 https://shardingsphere.apache.org/document/current/cn/user-manual/shardingsphere-jdbc/configuration/yaml/
 */
@Configuration
public class ShardingConfiguration {

    @Bean
    public SqlSessionTemplate sqlTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /*@Bean
    public DataSource shardingDataSource() throws IOException, SQLException {

        File yamlFile = new File(ShardingConfiguration.class.getResource("/META-INF/sharding.yaml").getFile());

        DataSource dataSource = YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);

        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("shardingDataSource") DataSource shardingDataSource) throws Exception {

        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();

        bean.setDataSource(shardingDataSource);

        return bean.getObject();
    }*/

}
