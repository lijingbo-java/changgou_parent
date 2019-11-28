package com.changgou.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/28 9:47
 * @description ：控制器
 * @version: 1.0
 */
@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemPageService itemPageService;
    @GetMapping
    @ResponseBody
    public Result staticPage(String supId) {
        itemPageService.staticPage(supId);

        return new Result(true, StatusCode.OK, "生成静态页面成功");
    }
}
