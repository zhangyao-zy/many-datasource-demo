package com.zhangyao.springboot.config;

import com.zaxxer.hikari.HikariDataSource;
import com.zhangyao.springboot.domin.Databaseinfo;
import com.zhangyao.springboot.service.DataBaseService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;

import static com.zhangyao.springboot.config.MyDynamicDataSource.DataSourceCache;

/**
 * @author: zhangyao
 * @create:2020-04-07 11:20
 **/
@Aspect
@Component
@Slf4j
public class DataSourceAop {
    @Autowired
    DataBaseService dataBaseService;

    @Resource(name = "dynamicDataSource")
    DynamicChangeDataSourceConfig dynamicChangeDataSourceConfig;
    /**
     * 定义切入点
     * 切入点为有该注解的方法
     * 此注解用于数据源TEST1
     */
    @Pointcut("@annotation(com.zhangyao.springboot.annotation.DataSourceServiceAop)")
    public void serviceTest1DatasourceAspect(){};

    /**
     * 在切入service方法之前执行
     * 设置数据源
     */
    @Before("serviceTest1DatasourceAspect()")
    public void beforeAspect(){
        log.info("切入方法,开始设置数据源");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String database_key = attributes.getRequest().getHeader("database_key");
        initDataSource(database_key);
        ThreadLocalDataSource.setLocalSource(database_key);


    }
    /**
     * 在切入service方法之后执行
     * 设置回默认数据源
     */
    @After("serviceTest1DatasourceAspect()")
    public void afterAspect(){
        log.info("切入方法后,开始切换默认数据源");
        ThreadLocalDataSource.setLocalSource("default");
    }


    public HikariDataSource initDataSource(String key) {
        HikariDataSource dataSource = new HikariDataSource();
        if ("default".equals(key)) {
            dataSource.setJdbcUrl("jdbc:mysql://192.168.164.134:3306/test1?useUnicode=true&characterEncoding=utf-8&useSSL=false");
            dataSource.setUsername("root");
            dataSource.setPassword("zhangyao");
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        } else {
            //查询数据库
            ThreadLocalDataSource.setLocalSource("default");
            Databaseinfo dataBaseInfo = dataBaseService.getDataBaseInfo(key);
            dataSource.setJdbcUrl(dataBaseInfo.getUrl());
            dataSource.setUsername(dataBaseInfo.getUserName());
            dataSource.setPassword(dataBaseInfo.getPassword());
            dataSource.setDriverClassName(dataBaseInfo.getDriverClassName());
            DataSourceCache.put(key, dataSource);
            dynamicChangeDataSourceConfig.setTargetDataSources(DataSourceCache);
            dynamicChangeDataSourceConfig.afterPropertiesSet();
            ThreadLocalDataSource.setLocalSource(key);
        }
        return dataSource;
    }
}
