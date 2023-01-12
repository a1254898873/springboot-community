package com.nifengi.community.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.nifengi.community.constant.CommunityConstant;
import com.nifengi.community.entity.User;
import com.nifengi.community.entity.request.FolloweRequest;
import com.nifengi.community.entity.request.UnFolloweRequest;
import com.nifengi.community.entity.response.JsonResult;
import com.nifengi.community.service.IFollowService;
import com.nifengi.community.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Yu
 * @title: FollowController
 * @projectName community
 * @date 2023/1/10 13:28
 */
@RequestMapping("/user")
@RestController
public class FollowController {

    @Autowired
    private IFollowService followService;

    @Autowired
    private IUserService userService;


    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    public JsonResult follow(@RequestBody FolloweRequest followeRequest) {
        if (StpUtil.isLogin() == false) {
            return JsonResult.fail("你还没有登录哦!");
        }

        int userId = StpUtil.getLoginIdAsInt();


        followService.follow(userId, followeRequest.getEntityType(), followeRequest.getEntityId());

        // 触发关注事件
//        Event event = new Event()
//                .setTopic(TOPIC_FOLLOW)
//                .setUserId(hostHolder.getUser().getId())
//                .setEntityType(entityType)
//                .setEntityId(entityId)
//                .setEntityUserId(entityId);
//        eventProducer.fireEvent(event);

        return JsonResult.success("已关注");
    }

    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    public JsonResult unfollow(@RequestBody UnFolloweRequest unFolloweRequest) {
        if (StpUtil.isLogin() == false) {
            return JsonResult.fail("你还没有登录哦!");
        }

        int userId = StpUtil.getLoginIdAsInt();

        followService.unfollow(userId, unFolloweRequest.getEntityType(), unFolloweRequest.getEntityId());

        return JsonResult.success("已取消关注");
    }

    @RequestMapping(path = "/followees/{userId}", method = RequestMethod.GET)
    public JsonResult<JSONObject> getFollowees(@PathVariable("userId") int userId, int current, int limit) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        JSONObject res = new JSONObject();
        res.put("user",user);


        List<Map<String, Object>> userList = followService.findFollowees(userId, current-1, limit);
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        res.put("users",userList);

        return JsonResult.success(res);
    }

    @RequestMapping(path = "/followers/{userId}", method = RequestMethod.GET)
    public JsonResult<JSONObject> getFollowers(@PathVariable("userId") int userId,  int current, int limit) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        JSONObject res = new JSONObject();
        res.put("user",user);

        List<Map<String, Object>> userList = followService.findFollowers(userId, current-1,limit);
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        res.put("users",userList);

        return JsonResult.success(res);
    }


    private boolean hasFollowed(int userId) {
        if (StpUtil.isLogin() == false) {
            return false;
        }
        return true;
    }
}
