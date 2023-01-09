package com.nifengi.community.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.nifengi.community.constant.CommunityConstant;
import com.nifengi.community.entity.request.LikeRequest;
import com.nifengi.community.entity.response.JsonResult;
import com.nifengi.community.service.ILikeService;
import com.nifengi.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yu
 * @title: LikeController
 * @projectName community
 * @date 2023/1/9 9:37
 */
@RestController
public class LikeController {


    @Autowired
    private ILikeService likeService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    public JsonResult like(@RequestBody LikeRequest likeRequest) {
        if (StpUtil.isLogin() == false) {
            return JsonResult.fail("你还没有登录哦!");
        }

        int userId = StpUtil.getLoginIdAsInt();


        // 点赞
        likeService.like(userId, likeRequest.getEntityType(), likeRequest.getEntityId(),likeRequest.getEntityUserId());

        // 数量
        long likeCount = likeService.findEntityLikeCount(likeRequest.getEntityType(), likeRequest.getEntityId());
        // 状态
        int likeStatus = likeService.findEntityLikeStatus(userId,likeRequest.getEntityType(), likeRequest.getEntityId());
        // 返回的结果
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        // 触发点赞事件
//        if (likeStatus == 1) {
//            Event event = new Event()
//                    .setTopic(TOPIC_LIKE)
//                    .setUserId(hostHolder.getUser().getId())
//                    .setEntityType(entityType)
//                    .setEntityId(entityId)
//                    .setEntityUserId(entityUserId)
//                    .setData("postId", postId);
//            eventProducer.fireEvent(event);
//        }

        if(likeRequest.getEntityType() == CommunityConstant.ENTITY_TYPE_POST) {
            // 计算帖子分数
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey, likeRequest.getPostId());
        }

        return JsonResult.success(map);
    }

}
