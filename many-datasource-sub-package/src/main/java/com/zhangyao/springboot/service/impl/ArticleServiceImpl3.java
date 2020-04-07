package com.zhangyao.springboot.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.zhangyao.springboot.domin.Article;
import com.zhangyao.springboot.service.ArticleService3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * jdbc方式使用dataSource
 * 这种情况下就不需要配置分包了
 * @author: zhangyao
 * @create:2020-04-07 08:42
 **/
@Service
@Repository
public class ArticleServiceImpl3 implements ArticleService3 {

    @Resource(name = "test2DataSource")
    DruidDataSource druidDataSource;

    @Resource(name="test2JdbcTemplate")
    JdbcTemplate jdbcTemplate;


    @Override
    public String testJdbcSql() {
        String sql = "select * from article";
//        List<Article> articles = jdbcTemplate.queryForList("select * from article", 8);
        List<Article> articles = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Article>(Article.class));
        return JSON.toJSONString(articles);

//        DruidPooledConnection connection = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet resultSet = null;
//        try {
//            connection = druidDataSource.getConnection();
//            String sql = "select * from article";
//            preparedStatement = connection.prepareStatement(sql);
//            resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()){
//                System.out.println(resultSet.getString("article_content"));
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                if(connection!=null){
//                    connection.close();
//                }
//                if(preparedStatement!=null){
//                    preparedStatement.close();
//                }
//                if(resultSet!=null){
//                    resultSet.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
    }
}
