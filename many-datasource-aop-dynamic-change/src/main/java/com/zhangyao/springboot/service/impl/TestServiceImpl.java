package com.zhangyao.springboot.service.impl;

import com.zhangyao.springboot.mapper.TestMapper;
import com.zhangyao.springboot.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: zhangyao
 * @create:2020-11-03 22:23
 * @Description:
 **/
@Service
@Transactional
public class TestServiceImpl implements TestService {

    @Autowired
    TestMapper testMapper;

    @Override
    public String test(String sql) {

        return testMapper.executeSql(sql);
    }
}
