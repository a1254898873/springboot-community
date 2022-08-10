package com.nifengi.community.service;

import com.nifengi.community.constant.CommunityConstant;
import com.nifengi.community.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nifeng
 * @since 2022-07-27
 */
public interface IUserService extends IService<User> {


    public Map<String, String> register(User user);

    public int activation(int userId, String code);

    public Map<String, Object> login(String username, String password);



}
