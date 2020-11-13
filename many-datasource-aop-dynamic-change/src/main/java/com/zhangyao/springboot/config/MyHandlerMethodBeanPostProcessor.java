package com.zhangyao.springboot.config;

import com.zhangyao.springboot.controller.MySpringMVCHandleMethod;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangyao
 * @create:2020-11-11 16:47
 * @Description:
 **/
@Component
public class MyHandlerMethodBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    MySpringMVCHandleMethod mySpringMVCHandleMethod;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof RequestMappingHandlerAdapter){
            RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
            List<HandlerMethodArgumentResolver> argumentResolvers = adapter.getArgumentResolvers();
            List<HandlerMethodArgumentResolver> argumentResolversMy = new ArrayList<>();
            argumentResolversMy.add(mySpringMVCHandleMethod);
            argumentResolversMy.addAll(argumentResolvers);
            adapter.setArgumentResolvers(argumentResolversMy);
        }

        return bean;
    }
}
