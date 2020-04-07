package com.zhangyao.springboot.controller;

import com.zhangyao.springboot.service.ArticleService;
import com.zhangyao.springboot.service.ArticleService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhangyao
 * @create:2020-04-06 22:43
 **/

@RestController
public class TestController {

    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleService2 articleService2;

    @GetMapping("/query")
    public String query(){
        return articleService.queryAll();
    }
    @GetMapping("/query2")
    public String query2(){
        return articleService2.queryAll();
    }
}
