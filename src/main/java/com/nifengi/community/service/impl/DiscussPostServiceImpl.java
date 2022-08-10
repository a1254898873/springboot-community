package com.nifengi.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nifengi.community.entity.DiscussPost;
import com.nifengi.community.mapper.DiscussPostMapper;
import com.nifengi.community.service.IDiscussPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nifengi.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nifeng
 * @since 2022-07-27
 */
@Service
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostMapper, DiscussPost> implements IDiscussPostService {

    @Autowired
    DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public IPage<DiscussPost> selectByPage(int current, int limit) {

        Page<DiscussPost> page = new Page<DiscussPost>(current, limit);


        IPage<DiscussPost> mapIPage = discussPostMapper.selectPage(page, new QueryWrapper<DiscussPost>().orderByDesc("id"));

        return mapIPage;


    }

    @Override
    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insert(post);
    }

    @Override
    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }
}
