package com.zhangyao.springboot.mapper;

import com.zhangyao.springboot.domin.Databaseinfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * @author: zhangyao
 * @create:2020-11-03 22:03
 * @Description:
 **/
@Repository
public interface DataBaseMapper extends BaseMapper<Databaseinfo> {
}
