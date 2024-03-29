package com.nifengi.community.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nifengi.community.constant.CommunityConstant;
import com.nifengi.community.entity.Message;
import com.nifengi.community.entity.User;
import com.nifengi.community.entity.request.MessageRequest;
import com.nifengi.community.service.IMessageService;
import com.nifengi.community.service.IUserService;
import com.nifengi.community.entity.response.JsonResult;
import com.nifengi.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

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

        int userId = StpUtil.getLoginIdAsInt();

        // 私信列表
        List<Message> letterList = messageService.findLetters(conversationId, current - 1, limit);
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null && letterList.size() != 0) {
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
        int id = CommunityUtil.getLetterTarget(conversationId, userId);
        res.put("target", userService.getById(id));

        // 设置已读
        List<Integer> ids = CommunityUtil.getLetterIds(letterList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return JsonResult.success(res);

    }




    @PostMapping(path = "/letter/send")
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

    @RequestMapping(path = "/notice/list", method = RequestMethod.GET)
    public JsonResult getNoticeList() {
        if (StpUtil.isLogin() == false) {
            return JsonResult.fail("你还没有登录哦!");
        }

        int userId = StpUtil.getLoginIdAsInt();

        JSONObject res = new JSONObject();

        // 查询评论类通知
        Message message = messageService.findLatestNotice(userId, CommunityConstant.TOPIC_COMMENT);
        if (message != null) {
            Map<String, Object> messageVO = new HashMap<>();
            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.getById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(userId, CommunityConstant.TOPIC_COMMENT);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnreadCount(userId, CommunityConstant.TOPIC_COMMENT);
            messageVO.put("unread", unread);

            res.put("commentNotice",messageVO);
        }

        // 查询点赞类通知
        message = messageService.findLatestNotice(userId, CommunityConstant.TOPIC_LIKE);
        if (message != null) {
            Map<String, Object> messageVO = new HashMap<>();
            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.getById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(userId, CommunityConstant.TOPIC_LIKE);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnreadCount(userId, CommunityConstant.TOPIC_LIKE);
            messageVO.put("unread", unread);

            res.put("likeNotice", messageVO);
        }

        // 查询关注类通知
        message = messageService.findLatestNotice(userId, CommunityConstant.TOPIC_FOLLOW);
        if (message != null) {
            Map<String, Object> messageVO = new HashMap<>();
            messageVO.put("message", message);
            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.getById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));

            int count = messageService.findNoticeCount(userId, CommunityConstant.TOPIC_FOLLOW);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnreadCount(userId, CommunityConstant.TOPIC_FOLLOW);
            messageVO.put("unread", unread);


            res.put("followNotice", messageVO);
        }

        // 查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(userId, null);
        res.put("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnreadCount(userId, null);
        res.put("noticeUnreadCount", noticeUnreadCount);

        return JsonResult.success(res);
    }

    @RequestMapping(path = "/notice/detail/{topic}", method = RequestMethod.GET)
    public JsonResult getNoticeDetail(@PathVariable("topic") String topic, int current, int limit) {
        if (StpUtil.isLogin() == false) {
            return JsonResult.fail("你还没有登录哦!");
        }

        int userId = StpUtil.getLoginIdAsInt();

        JSONObject res = new JSONObject();

        List<Message> noticeList = messageService.findNotices(userId, topic, current-1, limit);
        List<Map<String, Object>> noticeVoList = new ArrayList<>();
        if (noticeList != null) {
            for (Message notice : noticeList) {
                Map<String, Object> map = new HashMap<>();
                // 通知
                map.put("notice", notice);
                // 内容
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
                map.put("user", userService.getById((Integer) data.get("userId")));
                map.put("entityType", data.get("entityType"));
                map.put("entityId", data.get("entityId"));
                map.put("postId", data.get("postId"));
                // 通知作者
                map.put("fromUser", userService.getById(notice.getFromId()));

                noticeVoList.add(map);
            }
        }
        res.put("notices",noticeVoList);

        // 设置已读
        List<Integer> ids = CommunityUtil.getLetterIds(noticeList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return JsonResult.success(res);
    }



}
