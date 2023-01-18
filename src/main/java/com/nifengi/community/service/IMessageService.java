package com.nifengi.community.service;

import com.nifengi.community.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nifeng
 * @since 2022-07-27
 */
public interface IMessageService extends IService<Message> {


    public List<Message> findConversations(int userId, int offset, int limit);

    public int findConversationCount(int userId);

    public List<Message> findLetters(String conversationId, int offset, int limit);

    public int findLetterCount(String conversationId);

    public int findLetterUnreadCount(int userId, String conversationId);


    public int addMessage(Message message);

    public int addNotice(Message message);

    public int readMessage(List<Integer> ids);

    public Message findLatestNotice(int userId, String topic);

    public int findNoticeCount(int userId, String topic);

    public int findNoticeUnreadCount(int userId, String topic);

    public List<Message> findNotices(int userId, String topic, int offset, int limit);


}
