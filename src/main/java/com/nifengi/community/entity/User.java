package com.nifengi.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author nifeng
 * @since 2022-07-27
 */
@Getter
@Setter
@TableName("user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("salt")
    private String salt;

    @TableField("email")
    private String email;

    @ApiModelProperty("0-普通用户; 1-超级管理员; 2-版主;")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("0-未激活; 1-已激活;")
    @TableField("status")
    private Integer status;

    @TableField("activation_code")
    private String activationCode;

    @TableField("header_url")
    private String headerUrl;

    @TableField("create_time")
    private LocalDateTime createTime;


}
