package com.nifengi.community.util;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.nifengi.community.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.*;

/**
 * @author Yu
 * @title: CommunityUtil
 * @projectName community
 * @date 2022/7/28 16:20
 */

public class CommunityUtil {

    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5加密
    // hello -> abc123def456
    // hello + 3e4a8 -> abc123def456abc
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static int getLetterTarget(String conversationId, int userId) {
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);


        if (userId == id0) {
            return id1;
        } else {
            return id0;
        }
    }

    public static List<Integer> getLetterIds(List<Message> letterList) {
        List<Integer> ids = new ArrayList<>();

        int userId = StpUtil.getLoginIdAsInt();

        if (letterList != null && letterList.size() != 0) {
            for (Message message : letterList) {
                if (userId == message.getToId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }

        return ids;
    }




}
