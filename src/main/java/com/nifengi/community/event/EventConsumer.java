package com.nifengi.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nifengi.community.constant.CommunityConstant;
import com.nifengi.community.entity.DiscussPost;
import com.nifengi.community.entity.Event;
import com.nifengi.community.entity.Message;
import com.nifengi.community.service.IDiscussPostService;
import com.nifengi.community.service.IMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Yu
 * @title: EventConsumer
 * @projectName community
 * @date 2023/1/15 22:18
 */
@Component
public class EventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IDiscussPostService discussPostService;

//    @Autowired
//    private ElasticsearchService elasticsearchService;


    @RabbitListener(queues = CommunityConstant.TOPIC_COMMENT)
    @RabbitListener(queues = CommunityConstant.TOPIC_LIKE)
    @RabbitListener(queues = CommunityConstant.TOPIC_FOLLOW)
    public void handleCommentMessage(String jsonString) {
        if (jsonString == null) {
            logger.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(jsonString, Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        // 发送站内通知
        Message message = new Message();
        message.setFromId(CommunityConstant.SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(LocalDateTime.now());
        message.setStatus(0);

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }


        message.setContent(JSONObject.toJSONString(content));
        messageService.addNotice(message);
    }

//    // 消费发帖事件
//    @RabbitListener(queues = CommunityConstant.TOPIC_PUBLISH)
//    public void handlePublishMessage(String jsonString) {
//        if (jsonString== null) {
//            logger.error("消息的内容为空!");
//            return;
//        }
//
//        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
//        if (event == null) {
//            logger.error("消息格式错误!");
//            return;
//        }
//
//        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
//        elasticsearchService.saveDiscussPost(post);
//    }


}
