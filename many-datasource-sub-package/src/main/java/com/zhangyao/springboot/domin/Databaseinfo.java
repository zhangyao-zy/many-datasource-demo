package com.zhangyao.springboot.domin;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class Databaseinfo implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private Integer key;

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

    private static final long serialVersionUID = 1L;
}

