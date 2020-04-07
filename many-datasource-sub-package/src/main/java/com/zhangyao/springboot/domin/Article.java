package com.zhangyao.springboot.domin;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class Article implements Serializable {
    /**
     *
     */
    private Integer articleId;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 简要描述
     */
    private String articleDescribe;

    /**
     * 内容
     */
    private String articleContent;

    /**
     * 分类
     */
    private String articleType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 文章访问数量
     */
    private Integer accessNum;

    private static final long serialVersionUID = 1L;
}

