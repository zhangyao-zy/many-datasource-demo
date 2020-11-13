package com.zhangyao.springboot.domin;

import lombok.Data;

import java.util.Date;

@Data
public class Databaseinfo {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private String databaseKey;

    /**
     *
     */
    private String userName;

    /**
     *
     */
    private String password;

    /**
     *
     */
    private String url;

    /**
     *
     */
    private String driverClassName;

    /**
     *
     */
    private String type;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

}

