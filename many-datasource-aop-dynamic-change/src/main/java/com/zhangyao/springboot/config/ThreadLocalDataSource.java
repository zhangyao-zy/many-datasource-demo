package com.zhangyao.springboot.config;

import lombok.extern.slf4j.Slf4j;

import javax.xml.crypto.Data;

/**
 * ThreadLocal保存数据源的key,并切换清除
 * @author: zhangyao
 * @create:2020-04-07 09:24
 **/
@Slf4j
public class ThreadLocalDataSource {

    //使用threadLocal保证切换数据源时的线程安全 不会在多线程的情况下导致切换错数据源
    private static final ThreadLocal<String> TYPE = new ThreadLocal<String>();

    /**
     * 修改当前线程内的数据源id
     * @param key
     */
    public static void setLocalSource(String key){
        TYPE.set(key);
    }

    /**
     * 获取当前线程内的数据源类型
     * @return
     */
    public static String getLocalSource(){
        return TYPE.get();
    }

    /**
     * 清空ThreadLocal中的TYPE
     */
    public void clear(){
        TYPE.remove();
    }

}
