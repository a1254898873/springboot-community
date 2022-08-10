package com.nifengi.community.mapper;

import com.nifengi.community.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author nifeng
 * @since 2022-07-27
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

}
