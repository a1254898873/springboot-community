package com.nifengi.community.entity.request;

import lombok.Data;

/**
 * @author Yu
 * @title: CommentRequest
 * @projectName community
 * @date 2022/8/4 15:37
 */
@Data
public class CommentRequest {

    String content;

    Integer entityType;

    Integer entityId;

    Integer targetId;
}
