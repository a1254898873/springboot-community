package com.nifengi.community.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.nifengi.community.constant.CommunityConstant;
import com.nifengi.community.entity.Comment;
import com.nifengi.community.entity.DiscussPost;
import com.nifengi.community.entity.User;
import com.nifengi.community.service.ICommentService;
import com.nifengi.community.service.IDiscussPostService;
import com.nifengi.community.service.ILikeService;
import com.nifengi.community.service.IUserService;
import com.nifengi.community.entity.response.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DiscussPostController  {

    @Autowired
    private IDiscussPostService discussPostService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private ILikeService likeService;


    @PostMapping(path = "/add")
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
        if (StpUtil.isLogin() == false) {
            return JsonResult.fail("你还没有登录哦!");
        }
        int userId = StpUtil.getLoginIdAsInt();
        // 帖子
        DiscussPost post = discussPostService.getById(discussPostId);
        JSONObject res = new JSONObject();
        res.put("post",post);
        // 作者
        User user = userService.getById(post.getUserId());
        res.put("user",user);
        //点赞
        //点赞数量 1代表帖子 2代表评论
        long discussPostLikeCount = likeService.findEntityLikeCount(1, discussPostId);
        // 状态
        int discussPostLikeStatus = likeService.findEntityLikeStatus(userId, 1, discussPostId);
        // 返回的结果
        Map<String, Object> discussPostLike = new HashMap<>();
        discussPostLike.put("likeCount", discussPostLikeCount);
        discussPostLike.put("likeStatus", discussPostLikeStatus);
        res.put("like",discussPostLike);




        // 评论: 给帖子的评论
        // 回复: 给评论的评论
        // 评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(
             CommunityConstant.ENTITY_TYPE_POST, post.getId(), current-1, limit);
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
                        CommunityConstant.ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
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
                        //点赞数量 1代表帖子 2代表评论
                        long replyLikeCount = likeService.findEntityLikeCount(2, reply.getId());
                        // 状态
                        int replyLikeStatus = likeService.findEntityLikeStatus(userId, 2, reply.getId());
                        // 返回的结果
                        Map<String, Object> replyLike = new HashMap<>();
                        replyLike.put("likeCount", replyLikeCount);
                        replyLike.put("likeStatus", replyLikeStatus);
                        replyVo.put("like",replyLike);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(CommunityConstant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                //点赞数量 1代表帖子 2代表评论
                long commentLikeCount = likeService.findEntityLikeCount(2, comment.getId());
                // 状态
                int commentLikeStatus = likeService.findEntityLikeStatus(userId, 2, comment.getId());
                // 返回的结果
                Map<String, Object> commentLike = new HashMap<>();
                commentLike.put("likeCount", commentLikeCount);
                commentLike.put("likeStatus", commentLikeStatus);
                commentVo.put("like",commentLike);

                commentVoList.add(commentVo);
            }
        }

        res.put("comments",commentVoList);


        return JsonResult.success(res);
    }

}
