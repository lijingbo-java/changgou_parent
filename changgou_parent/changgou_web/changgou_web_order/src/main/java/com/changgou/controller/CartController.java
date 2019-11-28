package com.changgou.controller;

import com.changgou.order.feign.CartFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/9/4 10:43
 * @description ：
 * @version: 1.0
 */
@Controller
@RequestMapping("/wcart")
public class CartController {
    @Autowired
    private CartFeign cartFeign;
    @Autowired
    private RedisTemplate redisTemplate;


    //加入到购物车
    @RequestMapping("/addCart")
    public String add(String skuId, Integer id) {
        cartFeign.add(skuId, id);
        return "redirect://web.changgou.com:9102/wo/wcart/toCart";
    }

    @GetMapping("/toCart")
    public String toCart() {
        return "cart";
    }
}
