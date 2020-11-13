package com.zhangyao.springboot.service.impl;

import com.zhangyao.springboot.domin.Databaseinfo;
import com.zhangyao.springboot.mapper.DataBaseMapper;
import com.zhangyao.springboot.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: zhangyao
 * @create:2020-11-03 22:03
 * @Description:
 **/
@Service
@Transactional
public class DataBaseServiceImpl implements DataBaseService {

    @Autowired
    DataBaseMapper dataBaseMapper;

    /**
     * 查询数据源信息
     *
     * @param key
     * @return
     */
    @Override
    public Databaseinfo getDataBaseInfo(String key) {
        Databaseinfo databaseinfo = new Databaseinfo();
        databaseinfo.setDatabaseKey(key);
        return dataBaseMapper.selectOne(databaseinfo);
    }
}
