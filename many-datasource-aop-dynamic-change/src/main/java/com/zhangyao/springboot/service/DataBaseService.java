package com.zhangyao.springboot.service;

import com.zhangyao.springboot.domin.Databaseinfo;

/**
 * @author: zhangyao
 * @create:2020-11-03 21:59
 * @Description:
 **/
public interface DataBaseService {

    /**
     * 查询数据源信息
     * @param key
     * @return
     */
    Databaseinfo getDataBaseInfo(String key);


}
