package com.zhangyao.springboot.config;

import com.zhangyao.springboot.controller.MySpringMVCHandleMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangyao
 * @create:2020-11-10 15:47
 * @Description:
 **/
@Component
public class SpringMVCconfig implements WebMvcConfigurer {
    @Autowired
    MySpringMVCHandleMethod mySpringMVCHandleMethod;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(0, new MySpringMVCHandleMethod());
    }
}
