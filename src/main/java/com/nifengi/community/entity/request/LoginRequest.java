package com.nifengi.community.entity.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Yu
 * @title: LoginRequest
 * @projectName community
 * @date 2022/8/1 16:46
 */
@Data
public class LoginRequest {


    String username;


    String password;


}
