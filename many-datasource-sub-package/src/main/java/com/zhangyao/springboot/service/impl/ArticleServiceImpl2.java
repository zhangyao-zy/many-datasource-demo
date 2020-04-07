package com.zhangyao.springboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhangyao.springboot.mapper.test2.ArticleMapper2;
import com.zhangyao.springboot.service.ArticleService;
import com.zhangyao.springboot.service.ArticleService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @author: zhangyao
 * @create:2020-04-06 22:46
 **/

@Service
@Repository
public class ArticleServiceImpl2 implements ArticleService2 {

    @Autowired
    ArticleMapper2 articleMapper;

    @Override
    public String queryAll() {
        return JSON.toJSONString(articleMapper.selectAll());
    }
}
