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
