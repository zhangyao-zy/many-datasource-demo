package com.zhangyao.springboot.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * @author: zhangyao
 * @create:2020-11-03 22:24
 * @Description:
 **/
@Repository
public interface TestMapper {
    @Select("select test_info from test_info")
    String executeSql(String sql);
}
