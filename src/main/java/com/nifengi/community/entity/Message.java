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
@TableName("message")
@ApiModel(value = "Message对象", description = "")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("from_id")
    private Integer fromId;

    @TableField("to_id")
    private Integer toId;

    @TableField("conversation_id")
    private String conversationId;

    @TableField("content")
    private String content;

    @ApiModelProperty("0-未读;1-已读;2-删除;")
    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;


}
