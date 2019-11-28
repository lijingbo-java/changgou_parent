package com.changgou.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/22 21:26
 * @description ：Search的远程调用
 * @version: 1.0
 */
@FeignClient(name = "search")
@RequestMapping("/search")
public interface SearchFeign {
    @GetMapping
    public Map search(@RequestParam(name = "searchMap") Map<String,String> searchMap);
}
