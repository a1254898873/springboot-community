package com.nifengi.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nifengi.community.entity.DiscussPost;
import com.nifengi.community.entity.User;
import com.nifengi.community.service.IDiscussPostService;
import com.nifengi.community.service.IUserService;
import com.nifengi.community.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yu
 * @title: HomeController
 * @projectName community
 * @date 2022/7/27 12:00
 */
@Controller
@RestController
public class HomeController {

    @Autowired
    private IDiscussPostService discussPostService;

    @Autowired
    private IUserService userService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult<JSONObject> getIndexPage(int current, int limit) {
        IPage<DiscussPost> page = discussPostService.selectByPage(current,limit);
        List<JSONObject> discussPosts = new ArrayList<>();
        if (page != null) {
            for (DiscussPost post : page.getRecords()) {
                JSONObject obj = new JSONObject();
                obj.put("post", post);
                User user = userService.getById(post.getUserId());
                obj.put("user", user);
                discussPosts.add(obj);
            }
        }
        JSONObject res = new JSONObject();
        res.put("size",page.getSize());
        res.put("total",page.getTotal());
        res.put("pages",page.getPages());
        res.put("data",discussPosts);
        return JsonResult.success(res);
    }
}
