package com.nifengi.community.entity.request;

import lombok.Data;

/**
 * @author Yu
 * @title: LikeRequest
 * @projectName community
 * @date 2023/1/9 14:20
 */
@Data
public class LikeRequest {

    int entityType;
    int entityId;
    int entityUserId;
    int postId;
}
