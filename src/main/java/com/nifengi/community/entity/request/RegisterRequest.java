package com.nifengi.community.entity.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @author Yu
 * @title: RegisterRequest
 * @projectName community
 * @date 2022/8/1 9:51
 */

@Data
public class RegisterRequest {

    @NotEmpty(message = "用户名不能为空")
    String username;

    @NotEmpty(message = "密码不能为空")
    String password;

    @Email
    String email;

//    @NotEmpty
//    String codeKey;
//
//    @NotEmpty
//    String codeText;

}
