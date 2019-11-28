package com.changgou.search.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.feign.SkuFeign;
import com.changgou.pojo.Sku;
import com.changgou.search.mapper.SearchMapper;
import com.changgou.search.pojo.SkuInfo;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/29 21:44
 * @description ：MQ监听器
 * @version: 1.0
 */
@Controller
@RabbitListener(queues = {"add_es_queue"})
public class RabbitMQListener {
    @Autowired
    private SearchMapper searchMapper;

    @Autowired
    private SkuFeign skuFeign;

    @RabbitHandler
    public void addEsQueue(String spuId) {
        List<Sku> skuBySpuId = skuFeign.findSkuBySpuId(spuId);
        List<SkuInfo> skuInfos = JSON.parseArray(JSON.toJSONString(skuBySpuId), SkuInfo.class);
        searchMapper.saveAll(skuInfos);

    }

}
