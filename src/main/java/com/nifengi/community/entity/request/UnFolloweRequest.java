package com.nifengi.community.entity.request;

import lombok.Data;

/**
 * @author Yu
 * @title: FollowerRequest
 * @projectName community
 * @date 2023/1/12 0:21
 */
@Data
public class UnFolloweRequest {

    int entityType;
    int entityId;
}
