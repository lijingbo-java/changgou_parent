package com.changgou.business.feign;

import com.changgou.pojo.Ad;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/20 22:05
 * @description ：AdFeign
 * @version: 1.0
 */
@FeignClient(name = "business")
@RequestMapping("/ad")
public interface ADFeign {
    @GetMapping("/findAdListByPosition")
    public List<Ad> findAdListByPosition(@RequestParam(name = "position") String position);

}
