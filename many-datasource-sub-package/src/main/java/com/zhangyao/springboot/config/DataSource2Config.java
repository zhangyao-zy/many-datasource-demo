package com.zhangyao.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;


/**
 * @author: zhangyao
 * @create:2020-04-06 22:17
 **/

@Configuration
@MapperScan(basePackages = "com.zhangyao.springboot.mapper.test2",sqlSessionFactoryRef = "test2SqlSessionFactory")
public class DataSource2Config {

    @Bean(name = "test2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.test2")
    public DruidDataSource getDataSource2(){
        DruidDataSource datasourc =  DataSourceBuilder.create().type(DruidDataSource.class).build();
        return datasourc;
    }

    @Bean(name="test2JdbcTemplate")
    public JdbcTemplate getJdbcTemplte(@Qualifier("test2DataSource")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean("test2SqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory2(@Qualifier("test2DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
//        sqlSessionFactoryBean.setMapperLocations();

        return sqlSessionFactoryBean.getObject();
    }


    @Bean("test2SqlSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate2(@Qualifier("test2SqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
