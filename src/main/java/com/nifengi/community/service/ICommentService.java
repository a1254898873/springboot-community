package com.nifengi.community.service;

import com.nifengi.community.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nifeng
 * @since 2022-07-27
 */
public interface ICommentService extends IService<Comment> {

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit);

    public int findCommentCount(int entityType, int entityId);

    public int addComment(Comment comment);

}
