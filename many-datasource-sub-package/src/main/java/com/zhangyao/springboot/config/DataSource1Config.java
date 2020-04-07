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
 * 分包多数据源配置
 * 数据源1
 *
 * @author: zhangyao
 * @create:2020-04-06 22:17
 **/


@Configuration
//使用mybatis的话 通过mapperScan规定了不同的mapper包下使用的数据源
//sqlSessionFactoryRef执行sqlSessionFactory使用哪个数据源的
@MapperScan(basePackages = "com.zhangyao.springboot.mapper.test1",sqlSessionFactoryRef = "test1SqlSessionFactory")
public class DataSource1Config {

    //注入spring
    @Bean(name = "test1DataSource")
    //标记默认数据源
    @Primary
    //从配置文件中读取数据源配置 前缀格式不固定
    @ConfigurationProperties(prefix = "spring.datasource.test1")
    public DruidDataSource getDataSource1(){
        //这里使用的druid数据源,使用不同的数据源就创建不同的dataSource
        DruidDataSource datasourc =  DataSourceBuilder.create().type(DruidDataSource.class).build();
        return datasourc;
    }


    @Bean(name="test1JdbcTemplate")
    public JdbcTemplate getJdbcTemplte(@Qualifier("test1DataSource")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    //通过自定义的方式把dataSource配置信息注入sqlSessionFactory
    //这是mybatis需要使用的
    @Bean("test1SqlSessionFactory")
    @Primary
    public SqlSessionFactory getSqlSessionFactory1(@Qualifier("test1DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
//        sqlSessionFactoryBean.setMapperLocations();

        return sqlSessionFactoryBean.getObject();
    }


    /**
     * 同样是myBatis需要使用的 把sqlSessionFactory 注入sqlSessionTemplate
     * @param sqlSessionFactory
     * @return
     */
    @Bean("test1SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate getSqlSessionTemplate1(@Qualifier("test1SqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
