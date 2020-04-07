package com.zhangyao.springboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhangyao.springboot.mapper.test1.ArticleMapper;
import com.zhangyao.springboot.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * 多数据源分包方案
 * 引入哪个数据源包下的mapper 就默认使用哪个数据源
 * @author: zhangyao
 * @create:2020-04-06 22:46
 **/

@Service
@Repository
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Override
    public String queryAll() {
        return JSON.toJSONString(articleMapper.selectAll());
    }
}
