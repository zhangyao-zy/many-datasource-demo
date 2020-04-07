package com.zhangyao.springboot.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 继承AbStractRoutingDataSource
 * 动态切换数据源
 * @author: zhangyao
 * @create:2020-04-07 09:23
 **/
public class DynamicChangeDataSourceConfig extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceType.DataBaseType dataBaseType = DataSourceType.getDataBaseType();
        return dataBaseType;
    }
}
