package com.nifengi.community.service;

/**
 * @author Yu
 * @title: ILikeService
 * @projectName community
 * @date 2023/1/9 8:58
 */
public interface ILikeService {

    /**
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @param entityUserId
     * 点赞
     */
    public void like(int userId, int entityType, int entityId, int entityUserId);

    /**
     *
     * @param entityType
     * @param entityId
     * @return
     * 查询某实体点赞的数量
     */
    public long findEntityLikeCount(int entityType, int entityId);

    /**
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     * 查询某人对某实体点赞的状态
     */
    public int findEntityLikeStatus(int userId, int entityType, int entityId);


    /**
     *
     * @param userId
     * @return
     * 查询某个用户获得的赞
     */
    public int findUserLikeCount(int userId);

}
