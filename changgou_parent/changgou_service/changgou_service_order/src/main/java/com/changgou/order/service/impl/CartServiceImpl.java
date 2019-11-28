package com.changgou.order.service.impl;

import com.changgou.feign.SkuFeign;
import com.changgou.order.service.CartService;
import com.changgou.pojo.OrderItem;
import com.changgou.pojo.Sku;
import com.changgou.utils.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/9/4 15:32
 * @description ：添加商品到购物车
 * @version: 1.0
 */
@Service
@SuppressWarnings("all")
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SkuFeign skuFeign;
    String name = "sunwukong";

    @Override
    public void add(String skuId, Integer num) {
        //做标记使用
        boolean flag = false;
        List<OrderItem> orderItemList = redisTemplate.boundHashOps(CacheKey.CART_LIST + name).values();
        if (orderItemList != null && orderItemList.size() > 0) {
            for (OrderItem orderItem : orderItemList) {
                if (orderItem.getSkuId().equals(skuId)) {
                    flag = true;//如果添加的商品已经存在,flag变为true并且数量上做改变
                    orderItem.setNum(orderItem.getNum() + num);
                    redisTemplate.boundHashOps(CacheKey.CART_LIST + name).put(skuId, orderItem);
                    break;
                }
            }
            if (!flag) {
                OrderItem orderItem = new OrderItem();
                orderItem.setNum(num);
                orderItem.setSkuId(skuId);
                Sku sku = skuFeign.findSkuById(skuId);
                orderItem.setImage(sku.getImage());
                orderItem.setName(sku.getName());
                //单价
                orderItem.setPrice(sku.getPrice());
                //小计
                orderItem.setPayMoney(orderItem.getPayMoney() * orderItem.getNum());
                orderItem.setMoney(orderItem.getPayMoney() * orderItem.getNum());
                redisTemplate.boundHashOps(CacheKey.CART_LIST + name).put(skuId, orderItem);

            }
        } else {

            OrderItem orderItem = new OrderItem();
            orderItem.setNum(num);
            orderItem.setSkuId(skuId);
            Sku sku = skuFeign.findSkuById(skuId);
            orderItem.setImage(sku.getImage());
            orderItem.setName(sku.getName());
            //单价
            orderItem.setPrice(sku.getPrice());
            //小计
            orderItem.setPayMoney(orderItem.getPayMoney() * orderItem.getNum());
            orderItem.setMoney(orderItem.getPayMoney() * orderItem.getNum());
            redisTemplate.boundHashOps(CacheKey.CART_LIST + name).put(skuId, orderItem);

        }
    }
}
