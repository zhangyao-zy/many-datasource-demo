### 分包

在yml中定义多个自定义数据源属性

通过bean的方式自定义datasource注入进spring

#### 1.定义配置文件多数据源

前缀无所谓,多数据源就不再使用默认的属性配置了

```sql
spring.datasource.test1.url=jdbc:mysql://localhost:3306/blog?useUnicode=true&characterEncoding=utf-8&useSSL=false
spring.datasource.test1.username=****
spring.datasource.test1.password=****
spring.datasource.test1.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.test1.driver-class-name=com.mysql.cj.jdbc.Driver

spring.datasource.test2.url=jdbc:mysql://(ip):3306/blog?useUnicode=true&characterEncoding=utf-8&useSSL=false
spring.datasource.test2.username=****
spring.datasource.test2.password=****
spring.datasource.test2.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.test2.driver-class-name=com.mysql.cj.jdbc.Driver

server.port=10001
spring.application.name=sub-package

```



#### 2.定义bean注入spring

自定义要注入进spring容器的dataSource

```java
package com.zhangyao.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;


/**
 * 分包多数据源配置
 * 数据源1
 *
 * @author: zhangyao
 * @create:2020-04-06 22:17
 **/


@Configuration
//使用mybatis的话 通过mapperScan规定了不同的mapper包下使用的数据源
//sqlSessionFactoryRef执行sqlSessionFactory使用哪个数据源的
@MapperScan(basePackages = "com.zhangyao.springboot.mapper.test1",sqlSessionFactoryRef = "test1SqlSessionFactory")
public class DataSource1Config {

    //注入spring
    @Bean(name = "test1DataSource")
    //标记默认数据源
    @Primary
    //从配置文件中读取数据源配置 前缀格式不固定
    @ConfigurationProperties(prefix = "spring.datasource.test1")
    public DruidDataSource getDataSource1(){
        //这里使用的druid数据源,使用不同的数据源就创建不同的dataSource
        DruidDataSource datasourc =  DataSourceBuilder.create().type(DruidDataSource.class).build();
        return datasourc;
    }


    //通过自定义的方式把dataSource配置信息注入sqlSessionFactory
    //这是mybatis需要使用的
    @Bean("test1SqlSessionFactory")
    @Primary
    public SqlSessionFactory getSqlSessionFactory1(@Qualifier("test1DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
//        sqlSessionFactoryBean.setMapperLocations();

        return sqlSessionFactoryBean.getObject();
    }


    /**
     * 同样是myBatis需要使用的 把sqlSessionFactory 注入sqlSessionTemplate
     * @param sqlSessionFactory
     * @return
     */
    @Bean("test1SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate getSqlSessionTemplate1(@Qualifier("test1SqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

```

**这是第一个数据源的配置 也是主数据源的配置**

**如果不使用mybatis可以不用配置sqlSessionFactory 和 sqlSessionTemplate**



#### 3.使用(mybatis)

在业务代码中引入不同的mapper包下的mapper 就使用不同数据源

```java
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

```

这里使用的ArticleMapper 就是上述自定义主数据源配置类中对应的mapperScan扫描下的mapper

#### 4.使用(jdbc)

jdbc使用就直接使用@Resource获取对应名称的DataSource即可

如果多数据源使用方式为jdbc,可以不用定义mybatis一系列的Bean(sqlSessionFactory和sqlSessionTemplate)

```java
package com.zhangyao.springboot.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.zhangyao.springboot.service.ArticleService3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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


    @Override
    public String testJdbcSql() {
        DruidPooledConnection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = druidDataSource.getConnection();
            String sql = "select * from article";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getString("article_content"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if(connection!=null){
                    connection.close();
                }
                if(preparedStatement!=null){
                    preparedStatement.close();
                }
                if(resultSet!=null){
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

```



#### 5.使用(jdbcTemplate)

在第二步注入spring的配置文件中 增加两个数据源的jdbcTemplate Bean,参数就是对应数据源的dataSource

```java
@Bean(name="test1JdbcTemplate")
public JdbcTemplate getJdbcTemplte(@Qualifier("test1DataSource")DataSource dataSource){
    return new JdbcTemplate(dataSource);
}
```

使用时直接通过@Resource引入不同数据源对应的JdbcTemplate即可

```java
@Resource(name="test2JdbcTemplate")
JdbcTemplate jdbcTemplate;
```

在业务类中使用jdbcTemplate对应的方法





### aop动态切换

aop动态切换数据源实现基于 spring-jdbc 提供的一个类 **AbstractRoutingDataSource**

在这个类中把所有的数据源都放到一个map中,通过切换map的键来获取不同的dataSource

具体实现如下

#### 1.定义配置文件多数据源

同分包方案

#### 2.定义数据源类型操作类

**AbstractRoutingDataSource**通过一个map来控制数据源的切换,就需要定义一个map.key的操作类,通过切换key之后,就能切换数据源

把数据源类型放在线程安全的ThreadLocal中,保证了每个线程使用不同数据源时,不会切换混乱

```java
package com.zhangyao.springboot.config;

import lombok.extern.slf4j.Slf4j;

import javax.xml.crypto.Data;

/**
 * 这里用来定义有多个数据源
 * 设置数据源类型以及获取数据源类型
 * @author: zhangyao
 * @create:2020-04-07 09:24
 **/
@Slf4j
public class DataSourceType {

    //数据源类型
    public enum DataBaseType{
        TEST1,TEST2
    }

    //使用threadLocal保证切换数据源时的线程安全 不会在多线程的情况下导致切换错数据源
    private static final ThreadLocal<DataBaseType> TYPE = new ThreadLocal<DataBaseType>();

    /**
     * 切换数据源
     * @param dataBaseType
     */
    public static  void setDataBaseType(DataBaseType dataBaseType){
        if(dataBaseType==null){
            throw new NullPointerException();
        }

        log.info("切换数据源"+dataBaseType);

        TYPE.set(dataBaseType);
    }


    /**
     * 获取当前线程内的数据源类型
     * @return
     */
    public static DataBaseType getDataBaseType(){
        DataBaseType dataBaseType = TYPE.get()==null?DataBaseType.TEST1:TYPE.get();
        log.info("当前数据源"+ dataBaseType);
        return  dataBaseType;
    }

    /**
     * 清空ThreadLocal中的TYPE
     */
    public void clear(){
        TYPE.remove();
    }

}

```

#### 3.重写**AbstractRoutingDataSource** 方法

**AbstractRoutingDataSource** 是一个抽象类,需要重写**AbstractRoutingDataSource** 中的determineCurrentLookupKey方法

这个方法是用来获取当前使用的DataSource在map中的key的

```java
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

```

把我们上面定义的线程安全的ThreadLocal中的DataBaseType设置进去

#### 4.配置动态数据源bean

把重写后的**AbstractRoutingDataSource** 子类(DynamicChangeDataSourceConfig)注册进spring  把配置文件中定义的多数据源注入进去,生成一个动态的数据源

如下: 重点是**dynamicDataSource** 其他配置与分包方案类似

```java
package com.zhangyao.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * aop多数据源动态切换配置
 * @author: zhangyao
 * @create:2020-04-06 22:17
 **/

@Configuration
@MapperScan(basePackages = "com.zhangyao.springboot.mapper",sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSource1Config {

    /**
     * 主数据源配置
     * @return
     */
    @Bean(name = "test1DataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.test1")
    public DruidDataSource getDataSource1(){
        DruidDataSource datasourc =  DataSourceBuilder.create().type(DruidDataSource.class).build();
        return datasourc;
    }

    /**
     * 第二个数据源配置
     * @return
     */
    @Bean(name = "test2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.test2")
    public DruidDataSource getDataSource2(){
        DruidDataSource datasourc =  DataSourceBuilder.create().type(DruidDataSource.class).build();
        return datasourc;
    }


    /**
     * 动态装配所有的数据源
     * @param dataSource1
     * @param dataSource2
     * @return
     */
    @Bean("dynamicDataSource")
    public DynamicChangeDataSourceConfig setDynamicDataSource(@Qualifier("test1DataSource") DataSource dataSource1,
                                                              @Qualifier("test2DataSource") DataSource dataSource2){
        //定义所有的数据源
        Map<Object,Object> allDataSource = new HashMap<Object, Object>();
        //把配置的多数据源放入map
        allDataSource.put(DataSourceType.DataBaseType.TEST1, dataSource1);
        allDataSource.put(DataSourceType.DataBaseType.TEST2, dataSource2);

        //定义实现了AbstractDataSource的自定义aop切换类
        DynamicChangeDataSourceConfig dynamicChangeDataSourceConfig = new DynamicChangeDataSourceConfig();
        //把上面的所有的数据源的map放进去
        dynamicChangeDataSourceConfig.setTargetDataSources(allDataSource);
        //设置默认的数据源
        dynamicChangeDataSourceConfig.setDefaultTargetDataSource(dataSource1);

        return dynamicChangeDataSourceConfig;
    }



    @Bean("sqlSessionFactory")
    @Primary
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("dynamicDataSource") DynamicChangeDataSourceConfig dynamicChangeDataSourceConfig) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicChangeDataSourceConfig);
//        sqlSessionFactoryBean.setMapperLocations();

        return sqlSessionFactoryBean.getObject();
    }


    @Bean("sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate getSqlSessionTemplate1(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

```



#### 5.定义不同数据源的注解

定义不同数据源的注解,当这些注解放在service层的方法上时,通过aop来动态设置数据源

```java
package com.zhangyao.springboot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义数据源切面使用的注解
 * @author: zhangyao
 * @create:2020-04-07 11:30
 **/


//表示此注解可以使用的方法上
@Target({ElementType.METHOD})
//表示在程序运行期生效
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource2ServiceAop {
}

```

```java
package com.zhangyao.springboot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义数据源切面使用的注解
 * @author: zhangyao
 * @create:2020-04-07 11:30
 **/


//表示此注解可以使用的方法上
@Target({ElementType.METHOD})
//表示在程序运行期生效
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceServiceAop {
}

```

#### 6.开发aop切面

需要通过刚才的注解切入service方法,在方法执行前设置不同的数据源

```java
package com.zhangyao.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author: zhangyao
 * @create:2020-04-07 11:20
 **/
@Aspect
@Component
@Slf4j
public class DataSourceAop {

    /**
     * 定义切入点
     * 切入点为有该注解的方法
     * 此注解用于数据源TEST1
     */
    @Pointcut("@annotation(com.zhangyao.springboot.annotation.DataSourceServiceAop)")
    public void serviceTest1DatasourceAspect(){};
    /**
     * 定义切入点
     * 切入点为有该注解的方法
     * 此注解用于数据源TEST1
     */
    @Pointcut("@annotation(com.zhangyao.springboot.annotation.DataSource2ServiceAop)")
    public void serviceTest2DatasourceAspect(){};


    /**
     * 在切入service方法之前执行
     * 设置数据源TEST1
     */
    @Before("serviceTest1DatasourceAspect()")
    public void beforeAspect1(){
        log.info("切入方法,开始设置数据源TEST1");
        DataSourceType.setDataBaseType(DataSourceType.DataBaseType.TEST1);
    }
    /**
     * 在切入service方法之前执行
     * 设置数据源TEST2
     */
    @Before("serviceTest2DatasourceAspect()")
    public void beforeAspect2(){
        log.info("切入方法,开始设置数据源TEST2");
        DataSourceType.setDataBaseType(DataSourceType.DataBaseType.TEST2);
    }
}

```

#### 7.使用(mybatis)

```java
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

```

加上@DataSourceServiceAop就会进入aop,切换不同的数据源



#### 8.总结

​	aop动态切换数据源,实质上就是把继承了**AbstractRoutingDataSource** 的子类注入进spring,通过**AbstractRoutingDataSource** 类提供的方法来切换数据源

​	这是spring-jdbc提供的轻量级的多数据源切换解决方案

### 缺陷

1. 需要引入分布式事务,生命式事务在操作时会有事务回滚问题
2. 数据源较多时,重复代码冗余比较多,可以封装一个有参数的注解,把数据源传入