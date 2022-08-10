package com.nifengi.community.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.google.code.kaptcha.Producer;
import com.nifengi.community.constant.CommunityConstant;
import com.nifengi.community.entity.User;
import com.nifengi.community.entity.request.LoginRequest;
import com.nifengi.community.entity.request.RegisterRequest;
import com.nifengi.community.service.IUserService;
import com.nifengi.community.entity.response.JsonResult;
import com.nifengi.community.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Yu
 * @title: LoginController
 * @projectName community
 * @date 2022/7/28 13:27
 */
@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping(path = "/register")
    public JsonResult register(@Validated @RequestBody RegisterRequest registerRequest) {
        User user = new User();
        BeanUtils.copyProperties(registerRequest, user);
        Map<String, String> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            return JsonResult.success("注册成功,我们已经向您的邮箱发送了一封激活邮件,请尽快激活!");
        } else {
            if (map.containsKey("usernameMsg")) {
                return JsonResult.fail(map.get("usernameMsg"));
            }
            if (map.containsKey("passwordMsg")) {
                return JsonResult.fail(map.get("passwordMsg"));
            }

            if (map.containsKey("emailMsg")) {
                return JsonResult.fail(map.get("emailMsg"));
            }

            return JsonResult.fail("注册失败");
        }
    }

    // http://localhost:8080/community/activation/101/code
    @GetMapping(path = "/activation/{userId}/{code}")
    public JsonResult activation(@PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == CommunityConstant.ACTIVATION_SUCCESS) {
            return JsonResult.success("激活成功,您的账号已经可以正常使用了!");
        } else if (result == CommunityConstant.ACTIVATION_REPEAT) {
            return JsonResult.fail("无效操作,该账号已经激活过了!");
        } else {
            return JsonResult.fail("激活失败,您提供的激活码不正确!");
        }
    }

    @GetMapping(path = "/kaptcha/{codeKey}")
    public void getKaptcha(@PathVariable String codeKey, HttpServletResponse response) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        // session.setAttribute("kaptcha", text);

        // 将验证码存入Redis
        String redisKey = RedisKeyUtil.getKaptchaKey(codeKey);
        redisTemplate.opsForValue().set(redisKey, text, 60, TimeUnit.SECONDS);

        // 将突图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("响应验证码失败:" + e.getMessage());
        }
    }

    @PostMapping(path = "/login")
    public JsonResult login(@RequestBody LoginRequest loginRequest) {
        // 检查验证码
        // String kaptcha = (String) session.getAttribute("kaptcha");
//        String kaptcha = null;
//        if (StringUtils.isNotBlank(kaptchaOwner)) {
//            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
//            kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
//        }


        Map<String, Object> map = userService.login(loginRequest.getUsername(), loginRequest.getPassword());


        if (map.containsKey("usernameMsg")) {
            return JsonResult.fail((String) map.get("usernameMsg"));
        } else if (map.containsKey("passwordMsg")) {
            return JsonResult.fail((String) map.get("passwordMsg"));
        } else {
            return JsonResult.success(map);
        }

    }


    @GetMapping(path = "/logout")
    public JsonResult logout() {
        StpUtil.logout();
        return JsonResult.success("成功退出");
    }

}
