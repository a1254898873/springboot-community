package com.nifengi.community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nifengi.community.entity.DiscussPost;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nifeng
 * @since 2022-07-27
 */
public interface IDiscussPostService extends IService<DiscussPost> {

    public IPage<DiscussPost> selectByPage(int current, int limit);

    public int addDiscussPost(DiscussPost post);

    public int updateCommentCount(int id, int commentCount);

}
