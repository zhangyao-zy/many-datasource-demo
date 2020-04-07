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
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * aop多数据源动态切换配置
 * @author: zhangyao
 * @create:2020-04-06 22:17
 **/

@Configuration
@MapperScan(basePackages = "com.zhangyao.springboot.mapper",sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSource1Config {

    /**
     * 主数据源配置
     * @return
     */
    @Bean(name = "test1DataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.test1")
    public DruidDataSource getDataSource1(){
        DruidDataSource datasourc =  DataSourceBuilder.create().type(DruidDataSource.class).build();
        return datasourc;
    }

    /**
     * 第二个数据源配置
     * @return
     */
    @Bean(name = "test2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.test2")
    public DruidDataSource getDataSource2(){
        DruidDataSource datasourc =  DataSourceBuilder.create().type(DruidDataSource.class).build();
        return datasourc;
    }


    /**
     * 动态装配所有的数据源
     * @param dataSource1
     * @param dataSource2
     * @return
     */
    @Bean("dynamicDataSource")
    public DynamicChangeDataSourceConfig setDynamicDataSource(@Qualifier("test1DataSource") DataSource dataSource1,
                                                              @Qualifier("test2DataSource") DataSource dataSource2){
        //定义所有的数据源
        Map<Object,Object> allDataSource = new HashMap<Object, Object>();
        //把配置的多数据源放入map
        allDataSource.put(DataSourceType.DataBaseType.TEST1, dataSource1);
        allDataSource.put(DataSourceType.DataBaseType.TEST2, dataSource2);

        //定义实现了AbstractDataSource的自定义aop切换类
        DynamicChangeDataSourceConfig dynamicChangeDataSourceConfig = new DynamicChangeDataSourceConfig();
        //把上面的所有的数据源的map放进去
        dynamicChangeDataSourceConfig.setTargetDataSources(allDataSource);
        //设置默认的数据源
        dynamicChangeDataSourceConfig.setDefaultTargetDataSource(dataSource1);

        return dynamicChangeDataSourceConfig;
    }



    @Bean("sqlSessionFactory")
    @Primary
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("dynamicDataSource") DynamicChangeDataSourceConfig dynamicChangeDataSourceConfig) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicChangeDataSourceConfig);
//        sqlSessionFactoryBean.setMapperLocations();

        return sqlSessionFactoryBean.getObject();
    }


    @Bean("sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate getSqlSessionTemplate1(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
