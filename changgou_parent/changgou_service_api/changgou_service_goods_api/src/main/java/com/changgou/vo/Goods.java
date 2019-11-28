package com.changgou.vo;

import com.changgou.pojo.Sku;
import com.changgou.pojo.Spu;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/14 17:31
 * @description ：商品和库存表的包装对象
 * @version: 1.0
 */

public class Goods implements Serializable {
    private Spu spu;
    private List<Sku> skuList;

    public Spu getSpu() {
        return spu;
    }

    public void setSpu(Spu spu) {
        this.spu = spu;
    }

    public List<Sku> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<Sku> skuList) {
        this.skuList = skuList;
    }
}
