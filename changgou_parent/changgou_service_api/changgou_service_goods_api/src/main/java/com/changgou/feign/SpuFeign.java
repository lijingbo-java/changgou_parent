package com.changgou.feign;

import com.changgou.pojo.Spu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/28 20:28
 * @description ：
 * @version: 1.0
 */
@RequestMapping("/spu")
@FeignClient(name = "goods")//配置文件中的服务名
public interface SpuFeign {

    @GetMapping("/findSpuById")
    public Spu findSpuById(@RequestParam(name = "spuId") String spuId);
}
