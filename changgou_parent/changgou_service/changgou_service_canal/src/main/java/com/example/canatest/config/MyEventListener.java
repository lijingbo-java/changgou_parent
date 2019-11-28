package com.example.canatest.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.business.feign.ADFeign;
import com.changgou.pojo.Ad;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import com.xpand.starter.canal.annotation.UpdateListenPoint;
import jdk.nashorn.internal.runtime.linker.JavaAdapterServices;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author chen.qian
 * @date 2018/3/19
 */
@CanalEventListener
public class MyEventListener {
    @Autowired
    private ADFeign adFeign;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @UpdateListenPoint(destination = "example", schema = "changgou_goods", table = "tb_spu")
    public void onEvent1(CanalEntry.RowData rowData) {
        System.err.println("UpdateListenPoint");
       /* Map beforMap = new HashMap<>();
        rowData.getBeforeColumnsList().forEach(c -> beforMap.put(c.getName(), c.getValue()));
        Map afterMap = new HashMap<>();
        rowData.getAfterColumnsList().forEach((c) -> afterMap.put(c.getName(), c.getValue()));
        //1.确定是下架到上架吗?
        if ("0".equals(beforMap.get("is_marketable")) && "1".equals(afterMap.get("is_marketable"))) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.ADD_ES_QUEUE,afterMap.get("id"));

        }*/
        Map beforeMap = new HashMap<>();
        rowData.getBeforeColumnsList().forEach(c ->
                beforeMap.put(c.getName(),c.getValue())
        );
        Map afterMap = new HashMap<>();
        rowData.getAfterColumnsList().forEach((c) ->
                afterMap.put(c.getName(),c.getValue())
        );


        //1:确定是下架到上架吗？
        if("0".equals(beforeMap.get("is_marketable")) && "1".equals(afterMap.get("is_marketable"))){

            //2:发消息 spuId 到MQ
            rabbitTemplate.convertAndSend(RabbitMQConfig.ES_PAGE_EXCHANGE,"it.haha",afterMap.get("id"));
        }

    }
   /* @InsertListenPoint
    public void onEvent(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        rowData.getAfterColumnsList().forEach((c) -> System.err.println("By--Annotation: " + c.getName() + " ::   " + c.getValue()));
    }


    @DeleteListenPoint
    public void onEvent3(CanalEntry.EventType eventType) {
        System.err.println("DeleteListenPoint");
    }*/

    @ListenPoint(destination = "example", schema = "changgou_business", table = {"tb_ad"}, eventType = {CanalEntry.EventType.UPDATE, CanalEntry.EventType.DELETE, CanalEntry.EventType.INSERT})
    public void onEvent4(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.err.println("广告发生改变");
        String position = rowData.getAfterColumnsList()
                .stream()
                .filter(c -> "position".equals(c.getName()))
                .limit(1)
                .collect(toList())
                .get(0)
                .getValue();
        System.out.println("position:" + position);
        List<Ad> adList = adFeign.findAdListByPosition(position);
        //将集合转换成JSON字符串
        stringRedisTemplate.boundValueOps("ad_" + position).set(JSON.toJSONString(adList));
    }
}
