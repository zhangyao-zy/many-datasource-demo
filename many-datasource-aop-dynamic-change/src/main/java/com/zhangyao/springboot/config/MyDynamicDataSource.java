package com.zhangyao.springboot.config;

import com.zaxxer.hikari.HikariDataSource;
import com.zhangyao.springboot.domin.Databaseinfo;
import com.zhangyao.springboot.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhangyao
 * @create:2020-11-03 21:07
 * @Description:
 **/
public class MyDynamicDataSource extends HikariDataSource {

    @Autowired
    DataBaseService dataBaseService;



    /**
     * 定义缓存数据源的变量
     */
    public static final Map<Object, Object> DataSourceCache = new ConcurrentHashMap<Object, Object>();

    @Override
    public Connection getConnection() throws SQLException {
        String localSourceKey = ThreadLocalDataSource.getLocalSource();
        HikariDataSource dataSource = (HikariDataSource) DataSourceCache.get(localSourceKey);
        try {
            dataSource = initDataSource(localSourceKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSource.getConnection();
    }

    /**
     * 初始化DataSource
     * 当缓存中没有对应的数据源时,需要去默认数据源查询数据库
     *
     * @param key
     * @return
     */
    public HikariDataSource initDataSource(String key) throws IOException {
        HikariDataSource dataSource = new HikariDataSource();
        if ("default".equals(key)) {
            Properties properties = PropertiesLoaderUtils.loadProperties(new EncodedResource(new ClassPathResource("application.properties"), "UTF-8"));
            dataSource.setJdbcUrl(properties.getProperty("spring.datasource.test1.jdbc-url"));
            dataSource.setUsername(properties.getProperty("spring.datasource.test1.username"));
            dataSource.setPassword(properties.getProperty("spring.datasource.test1.password"));
            dataSource.setDriverClassName(properties.getProperty("spring.datasource.test1.driver-class-name"));
        } else {
            //查询数据库
            ThreadLocalDataSource.setLocalSource("default");
            Databaseinfo dataBaseInfo = dataBaseService.getDataBaseInfo(key);
            dataSource.setJdbcUrl(dataBaseInfo.getUrl());
            dataSource.setUsername(dataBaseInfo.getUserName());
            dataSource.setPassword(dataBaseInfo.getPassword());
            dataSource.setDriverClassName(dataBaseInfo.getDriverClassName());
            ThreadLocalDataSource.setLocalSource(key);
        }
        DataSourceCache.put(key, dataSource);
        return dataSource;
    }
}
