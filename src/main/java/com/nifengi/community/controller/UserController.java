package com.nifengi.community.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.nifengi.community.constant.CommunityConstant;
import com.nifengi.community.entity.User;
import com.nifengi.community.service.IFollowService;
import com.nifengi.community.service.ILikeService;
import com.nifengi.community.service.IUserService;
import com.nifengi.community.entity.response.JsonResult;
import com.nifengi.community.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author nifeng
 * @since 2022-07-27
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    IUserService userService;

    @Autowired
    ILikeService likeService;

    @Autowired
    IFollowService followService;

    @Autowired
    MinioUtil minioUtils;


    @RequestMapping(value = "/upload",method = RequestMethod.GET)
    //用户头像上传访问方法
    public JsonResult upload(@RequestParam(value = "file") MultipartFile file) throws Exception {
        // 为了避免文件名重复，使用UUID重命名文件，将横杠去掉
        String fileName = UUID.randomUUID().toString().replace("-", "") + ".jpg";
        // 上传
        minioUtils.putObject(file.getInputStream(), fileName, file.getContentType());
        String url = minioUtils.getPath() + fileName;
        //更新用户头像
        User user = new User();
        user.setId(StpUtil.getLoginIdAsInt());
        user.setHeaderUrl(url);
        userService.updateById(user);
        return  JsonResult.success("上传头像成功");
    }


    // 个人主页
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public JsonResult<JSONObject> getProfilePage(@PathVariable("userId") int userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }

        JSONObject res = new JSONObject();

        // 用户
        res.put("user",user);
        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        res.put("likeCount",likeCount);

        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, CommunityConstant.ENTITY_TYPE_USER);
        res.put("followeeCount",followeeCount);

        // 粉丝数量
        long followerCount = followService.findFollowerCount(CommunityConstant.ENTITY_TYPE_USER, userId);
        res.put("followerCount",followerCount);

        // 是否已关注
        boolean hasFollowed = false;
        if (StpUtil.isLogin() == true) {
            int id = StpUtil.getLoginIdAsInt();
            hasFollowed = followService.hasFollowed(id, CommunityConstant.ENTITY_TYPE_USER, userId);
        }
        res.put("hasFollowed",hasFollowed);

        return JsonResult.success(res);
    }



}
