package com.changgou.order.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author ：jingbo.li
 * @date ：Created in 2019/9/4 19:38
 * @description ：暴露,调用
 * @version: 1.0
 */
@FeignClient(name = "order")
@RequestMapping("/cart")
public interface CartFeign {
    @GetMapping("/add")
    public Result add(@RequestParam(name = "skuId") String skuId, @RequestParam(name = "num") Integer num);
}
