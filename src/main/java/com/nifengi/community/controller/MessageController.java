package com.nifengi.community.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nifengi.community.entity.Message;
import com.nifengi.community.entity.User;
import com.nifengi.community.entity.request.MessageRequest;
import com.nifengi.community.service.IMessageService;
import com.nifengi.community.service.IUserService;
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
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private IMessageService messageService;


    @Autowired
    private IUserService userService;


    // 私信列表
    @GetMapping("/letter/list")
    public JsonResult getLetterList(int current, int limit) {

        if (StpUtil.isLogin() == false) {
            return JsonResult.fail("你还没有登录哦!");
        }
        int userId = StpUtil.getLoginIdAsInt();


        // 会话列表
        List<Message> conversationList = messageService.findConversations(
                userId, current - 1, limit);
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount", messageService.findLetterUnreadCount(userId, message.getConversationId()));
                int targetId = userId == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.getById(targetId));

                conversations.add(map);
            }
        }
        JSONObject res = new JSONObject();
        res.put("conversations", conversations);

        // 查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(userId, null);
        res.put("letterUnreadCount", letterUnreadCount);

        return JsonResult.success(res);
    }

    @GetMapping(path = "/letter/detail/{conversationId}")
    public JsonResult getLetterDetail(@PathVariable("conversationId") String conversationId, int current, int limit) {

        if (StpUtil.isLogin() == false) {
            return JsonResult.fail("你还没有登录哦!");
        }

        // 私信列表
        List<Message> letterList = messageService.findLetters(conversationId, current-1, limit);
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null && letterList.size()!=0) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.getById(message.getFromId()));
                letters.add(map);
            }
        }

        JSONObject res = new JSONObject();
        res.put("letters", letters);
        // 私信目标
        res.put("target", getLetterTarget(conversationId));

        // 设置已读
        List<Integer> ids = getLetterIds(letterList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return JsonResult.success(res);

    }

    private User getLetterTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);


        int userId = StpUtil.getLoginIdAsInt();

        if (userId == id0) {
            return userService.getById(id1);
        } else {
            return userService.getById(id0);
        }
    }

    private List<Integer> getLetterIds(List<Message> letterList) {
        List<Integer> ids = new ArrayList<>();

        int userId = StpUtil.getLoginIdAsInt();

        if (letterList != null && letterList.size()!=0) {
            for (Message message : letterList) {
                if (userId == message.getToId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }

        return ids;
    }


    @RequestMapping(path = "/letter/send", method = RequestMethod.POST)
    public JsonResult sendLetter(@RequestBody MessageRequest messageRequest) {

        if (StpUtil.isLogin() == false) {
            return JsonResult.fail("你还没有登录哦!");
        }

        int userId = StpUtil.getLoginIdAsInt();

        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("username", messageRequest.getToName());

        User target = userService.getOne(userWrapper);
        if (target == null) {
            return JsonResult.fail("目标用户不存在");
        }

        Message message = new Message();
        message.setFromId(userId);
        message.setToId(target.getId());
        message.setStatus(0);
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(messageRequest.getContent());
        message.setCreateTime(LocalDateTime.now());
        messageService.addMessage(message);

        return JsonResult.success("发送成功");

    }

}
