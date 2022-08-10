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
@TableName("login_ticket")
@ApiModel(value = "LoginTicket对象", description = "")
public class LoginTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("ticket")
    private String ticket;

    @ApiModelProperty("0-有效; 1-无效;")
    @TableField("status")
    private Integer status;

    @TableField("expired")
    private LocalDateTime expired;


}
