package com.nifengi.community.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nifengi.community.constant.CommunityConstant;
import com.nifengi.community.entity.Comment;
import com.nifengi.community.entity.DiscussPost;
import com.nifengi.community.entity.User;
import com.nifengi.community.service.ICommentService;
import com.nifengi.community.service.IDiscussPostService;
import com.nifengi.community.service.IUserService;
import com.nifengi.community.util.CommunityUtil;
import com.nifengi.community.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author nifeng
 * @since 2022-07-27
 */
@RestController
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private IDiscussPostService discussPostService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICommentService commentService;


    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addDiscussPost(String title, String content) {
        if (StpUtil.isLogin() == false) {
            return JsonResult.fail("你还没有登录哦!");
        }
        int userId = StpUtil.getLoginIdAsInt();

        DiscussPost post = new DiscussPost();
        post.setUserId(userId);
        post.setTitle(title);
        post.setContent(content);
        post.setCommentCount(0);
        post.setStatus(0);
        post.setType(0);
        post.setCreateTime(LocalDateTime.now());
        discussPostService.addDiscussPost(post);

        // 报错的情况,将来统一处理.
        return JsonResult.success("发布成功");
    }


    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public JsonResult getDiscussPost(@PathVariable("discussPostId") int discussPostId, int current,int limit) {
        // 帖子
        DiscussPost post = discussPostService.getById(discussPostId);
        JSONObject res = new JSONObject();
        res.put("post",post);
        // 作者
        User user = userService.getById(post.getUserId());
        res.put("user",user);




        // 评论: 给帖子的评论
        // 回复: 给评论的评论
        // 评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(
                ENTITY_TYPE_POST, post.getId(), current-1, limit);
        // 评论VO列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 评论VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者
                commentVo.put("user", userService.getById(comment.getUserId()));

                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.getById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.getById(reply.getTargetId());
                        replyVo.put("target", target);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }

        res.put("comments",commentVoList);


        return JsonResult.success(res);
    }

}
