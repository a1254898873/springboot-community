package com.nifengi.community.entity.request;

import lombok.Data;

/**
 * @author Yu
 * @title: MessageRequest
 * @projectName community
 * @date 2022/8/8 11:50
 */

@Data
public class MessageRequest {

    String toName;

    String content;

}
