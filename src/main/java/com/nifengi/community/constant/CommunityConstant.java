package com.nifengi.community.constant;

/**
 * @author Yu
 * @title: CommunityConstant
 * @projectName community
 * @date 2022/7/27 16:46
 */
public class CommunityConstant {

    /**
     * 激活成功
     */
    public final static int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    public final static int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    public final static int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态的登录凭证的超时时间
     */
    public final static int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住状态的登录凭证超时时间
     */
    public final static int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    /**
     * 实体类型: 帖子
     */
    public final static int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型: 评论
     */
    public final static int ENTITY_TYPE_COMMENT = 2;

}
