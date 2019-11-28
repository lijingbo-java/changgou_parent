package com.changgou.oauth2.controller;

import com.changgou.oauth2.pojo.AuthToken;
import com.changgou.oauth2.service.LoginServiceImpl;
import com.changgou.oauth2.util.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/9/1 9:44
 * @description ：登录控制中心
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginServiceImpl loginService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${auth.ttl}")
    private Integer ttl;
    @Value("${auth.cookieDomain}")
    private String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    private Integer cookieMaxAge;
    @GetMapping
    public String login(String ReturnUrl, Model model) {
        model.addAttribute("ReturnUrl", ReturnUrl);
        return "login";
    }

    @PostMapping
    public String login(String username, String password, String ReturnUrl) {
        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("用户名不允许为空");
        }
        if (StringUtils.isEmpty(password)) {
            throw new RuntimeException("用户秘密不允许为空");
        }
        //调用service层方法得到tonken
        AuthToken authToken = loginService.apply(username, password);
        //将长令牌放到redis中
        stringRedisTemplate.boundValueOps(authToken.getJti()).set(authToken.getAccessToken(),ttl,TimeUnit.SECONDS);
        //将短令牌放到cookice中
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        CookieUtil.addCookie(requestAttributes.getResponse(),cookieDomain,"/","uid",authToken.getJti(),cookieMaxAge,false);


        return "redirect:" + ReturnUrl;
    }

}
