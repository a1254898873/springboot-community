package com.nifengi.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nifengi.community.entity.Event;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Yu
 * @title: EventProducer
 * @projectName community
 * @date 2023/1/15 18:06
 */
@Component
public class EventProducer {

    @Autowired
    private AmqpTemplate template;

    // 处理事件
    public void fireEvent(Event event) {
        // 将事件发布到指定的主题
        template.convertAndSend(event.getTopic(), JSONObject.toJSONString(event));

    }


}
