package com.nifengi.community.config;

import com.nifengi.community.constant.CommunityConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yu
 * @title: RabbitMQConfig
 * @projectName community
 * @date 2023/1/15 20:50
 */
@Configuration
public class RabbitMQConfig {

    //交换机和队列要进行绑定:
    //默认的交换机是DirectExchange,
    //每个交换机都需要利用路由键来和队列绑定在一起.
    //如果采用的是DirectExchange交换机,默认情况下,队里的名字就是路由键的名字.
    //该交换机是一对一的,一个消息被发送者发送出去之后,只能被一个消费者接受.
    @Bean
    public Queue comment(){
        //comment,是队列的名字,
        return new Queue(CommunityConstant.TOPIC_COMMENT);
    }

    @Bean
    public Queue like(){
        //like,是队列的名字,
        return new Queue(CommunityConstant.TOPIC_LIKE);
    }

    @Bean
    public Queue follow(){
        //follow,是队列的名字,
        return new Queue(CommunityConstant.TOPIC_FOLLOW);
    }

}
