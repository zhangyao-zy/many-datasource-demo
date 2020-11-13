package com.zhangyao.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.zhangyao.springboot.config.MyDynamicDataSource.DataSourceCache;


/**
 *
 * aop多数据源动态切换配置
 * @author: zhangyao
 * @create:2020-04-06 22:17
 **/

@Configuration
@MapperScan(basePackages = "com.zhangyao.springboot.mapper",sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {

    /**
     * 主数据源配置
     * @return
     */
    @Bean(name = "primaryDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.test1")
    public DataSource getDataSource1(){
        HikariDataSource datasource =  DataSourceBuilder.create().type(HikariDataSource.class).build();
        if(datasource==null){
            try {
                datasource = new MyDynamicDataSource().initDataSource("default");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //设置默认的数据源
        DataSourceCache.put("default", datasource);
        ThreadLocalDataSource.setLocalSource("default");
        return datasource;
    }
    /**
     * 动态装配所有的数据源
     * @param primaryDataSource
     * @return
     */
    @Bean("dynamicDataSource")
    public DynamicChangeDataSourceConfig setDynamicDataSource(@Qualifier("primaryDataSource") DataSource primaryDataSource){
        //定义所有的数据源
        Map<Object,Object> allDataSource = new HashMap<Object, Object>();
        //把配置的多数据源放入map
        allDataSource.put("default", primaryDataSource);

        //定义实现了AbstractDataSource的自定义aop切换类
        DynamicChangeDataSourceConfig dynamicChangeDataSourceConfig = new DynamicChangeDataSourceConfig();
        //把上面的所有的数据源的map放进去
        dynamicChangeDataSourceConfig.setTargetDataSources(allDataSource);
        //设置默认的数据源
        dynamicChangeDataSourceConfig.setDefaultTargetDataSource(primaryDataSource);

        return dynamicChangeDataSourceConfig;
    }

    @Bean("sqlSessionFactory")
    @Primary
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        return sqlSessionFactoryBean.getObject();
    }



}
