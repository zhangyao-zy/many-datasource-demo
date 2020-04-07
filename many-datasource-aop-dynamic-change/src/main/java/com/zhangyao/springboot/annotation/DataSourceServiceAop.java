package com.zhangyao.springboot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义数据源切面使用的注解
 * @author: zhangyao
 * @create:2020-04-07 11:30
 **/


//表示此注解可以使用的方法上
@Target({ElementType.METHOD})
//表示在程序运行期生效
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceServiceAop {
}
