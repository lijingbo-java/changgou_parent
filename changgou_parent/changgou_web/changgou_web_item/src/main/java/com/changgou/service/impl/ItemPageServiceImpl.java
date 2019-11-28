package com.changgou.service.impl;


import com.alibaba.fastjson.JSON;
import com.changgou.feign.CategoryFeign;
import com.changgou.feign.SkuFeign;
import com.changgou.feign.SpuFeign;
import com.changgou.pojo.Category;
import com.changgou.pojo.Sku;
import com.changgou.pojo.Spu;
import com.changgou.service.ItemPageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/28 11:14
 * @description ：生成静态页面service实现层
 * @version: 1.0
 */
@Service
public class ItemPageServiceImpl implements ItemPageService {
    //模板引擎
    @Autowired
    private TemplateEngine templateEngine;
    //生成静态页的位置
    @Value("${pagepath}")
    public String pagepath;
    @Autowired
    private SpuFeign spuFeign;
    @Autowired
    private CategoryFeign categoryFeign;
    @Autowired
    private SkuFeign skuFeign;
    @Override
    public void staticPage(String spuId) {

        HashMap map = new HashMap<>();
        Spu spu = spuFeign.findSpuById(spuId);
        map.put("spu",spu);
        //商品分类名称集合
        Category category1 = categoryFeign.findCategoryById(spu.getCategory1Id());
        Category category2 = categoryFeign.findCategoryById(spu.getCategory2Id());
        Category category3 = categoryFeign.findCategoryById(spu.getCategory3Id());
        map.put("category1", category1);
        map.put("category2", category2);
        map.put("category3", category3);
        //库存集合查询
        List<Sku> skuList = skuFeign.findSkuBySpuId(spuId);
        map.put("skuList",skuList);

        //图片集合
        String images = spu.getImages();
        if (!StringUtils.isEmpty(images)){
            String[] split = images.split(",");
            map.put("imageList",split);
        }
        //库存规格集合
        String specItems = spu.getSpecItems();
        Map map1 = JSON.parseObject(specItems, Map.class);
        map.put("specificationList",map1);
        Context context = new Context();
        context.setVariables(map);
        //判断文件是否存在,如果不存在就创建一个
        File file = new File(pagepath);
        if (!file.exists()) {
            file.mkdirs();
        }

        Writer writer = null;

        try {
            writer = new PrintWriter(pagepath + "/" + spuId + ".html", "utf-8");
            //context是数据  writer是输出流, "item"是模板名(取出前后缀的页面名字)
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
