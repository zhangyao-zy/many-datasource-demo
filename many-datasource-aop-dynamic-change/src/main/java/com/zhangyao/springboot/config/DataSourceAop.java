package com.zhangyao.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author: zhangyao
 * @create:2020-04-07 11:20
 **/
@Aspect
@Component
@Slf4j
public class DataSourceAop {

    /**
     * 定义切入点
     * 切入点为有该注解的方法
     * 此注解用于数据源TEST1
     */
    @Pointcut("@annotation(com.zhangyao.springboot.annotation.DataSourceServiceAop)")
    public void serviceTest1DatasourceAspect(){};
    /**
     * 定义切入点
     * 切入点为有该注解的方法
     * 此注解用于数据源TEST1
     */
    @Pointcut("@annotation(com.zhangyao.springboot.annotation.DataSource2ServiceAop)")
    public void serviceTest2DatasourceAspect(){};


    /**
     * 在切入service方法之前执行
     * 设置数据源TEST1
     */
    @Before("serviceTest1DatasourceAspect()")
    public void beforeAspect1(){
        log.info("切入方法,开始设置数据源TEST1");
        DataSourceType.setDataBaseType(DataSourceType.DataBaseType.TEST1);
    }
    /**
     * 在切入service方法之前执行
     * 设置数据源TEST2
     */
    @Before("serviceTest2DatasourceAspect()")
    public void beforeAspect2(){
        log.info("切入方法,开始设置数据源TEST2");
        DataSourceType.setDataBaseType(DataSourceType.DataBaseType.TEST2);
    }
}
