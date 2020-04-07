package com.zhangyao.springboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhangyao.springboot.annotation.DataSourceServiceAop;
import com.zhangyao.springboot.mapper.test1.ArticleMapper;
import com.zhangyao.springboot.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 *
 * @author: zhangyao
 * @create:2020-04-06 22:46
 **/

@Service
@Repository
public class ArticleServiceImpl implements ArticleService {


    @Autowired
    ArticleMapper articleMapper;

    @Override
    @DataSourceServiceAop
    public String queryAll() {
        return JSON.toJSONString(articleMapper.selectAll());
    }
}
