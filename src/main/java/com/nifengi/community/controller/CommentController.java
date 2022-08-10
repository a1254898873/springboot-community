package com.nifengi.community.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.nifengi.community.entity.Comment;
import com.nifengi.community.entity.request.CommentRequest;
import com.nifengi.community.service.ICommentService;
import com.nifengi.community.entity.response.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author nifeng
 * @since 2022-07-27
 */

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @PostMapping(path = "/add")
    public JsonResult addComment(@RequestBody CommentRequest commentRequest) {

        if (StpUtil.isLogin() == false) {
            return JsonResult.fail("你还没有登录哦!");
        }
        int userId = StpUtil.getLoginIdAsInt();
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentRequest, comment);
        comment.setUserId(userId);
        comment.setStatus(0);
        comment.setCreateTime(LocalDateTime.now());
        commentService.addComment(comment);

        return JsonResult.success("发布评论成功");
    }

}
