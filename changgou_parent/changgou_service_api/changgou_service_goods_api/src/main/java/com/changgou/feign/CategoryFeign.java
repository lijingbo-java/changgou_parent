package com.changgou.feign;

import com.changgou.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/28 21:04
 * @description ：
 * @version: 1.0
 */
@RequestMapping("/category")
@FeignClient(name = "goods")
public interface CategoryFeign {
    @GetMapping("/findCategoryById")
    public Category findCategoryById(@RequestParam(name = "id") Integer id);

}
