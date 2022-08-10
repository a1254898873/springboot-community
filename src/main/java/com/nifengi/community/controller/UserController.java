package com.nifengi.community.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.nifengi.community.entity.User;
import com.nifengi.community.service.IUserService;
import com.nifengi.community.entity.response.JsonResult;
import com.nifengi.community.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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


}
