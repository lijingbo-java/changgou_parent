package com.changgou.feign;

import com.changgou.entity.Result;
import com.changgou.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/22 9:41
 * @description ：sku使用feign远程调用
 * @version: 1.0
 */
@FeignClient(name = "goods")
@RequestMapping("/sku")
public interface SkuFeign {
    @GetMapping
    public Result findAll();

    @GetMapping("/findSkuListBySpuId")
    public List<Sku> findSkuBySpuId(@RequestParam(name = "spuId") String spuId);

    @GetMapping("/findSkuById")
    public Sku findSkuById(@RequestParam(name = "id") String id);

}
