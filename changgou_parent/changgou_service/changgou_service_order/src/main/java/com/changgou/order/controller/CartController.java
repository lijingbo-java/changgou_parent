package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/9/4 15:29
 * @description ：购物车控制器
 * @version: 1.0
 */
@ResponseBody
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/add")
    public Result add(String skuId, Integer num) {
        cartService.add(skuId, num);
        return new Result(true, StatusCode.OK, "加入购物车成功");

    }
}
