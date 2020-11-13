package com.zhangyao.springboot.controller;

import com.zhangyao.springboot.annotation.DataSourceServiceAop;
import com.zhangyao.springboot.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: zhangyao
 * @create:2020-04-06 22:43
 **/

@RestController
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("/test")
    @DataSourceServiceAop
    public String test(String sql){
        return testService.test(sql);
    }


    @PostMapping("/test")
    public String test1(@RequestBody List list){
        list.forEach(a -> {
            System.out.println(a);
        });
        return "1";
    }
}
