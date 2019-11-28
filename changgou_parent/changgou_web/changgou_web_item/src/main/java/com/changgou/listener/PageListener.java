package com.changgou.listener;

import com.changgou.controller.ItemController;
import com.changgou.service.ItemPageService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/30 17:38
 * @description ：
 * @version: 1.0
 */
@Controller
@RabbitListener(queues = {"cretae_page_queue"})
public class PageListener {
    @Autowired
    private ItemPageService itemPageService;

    @RabbitHandler
    public void creationPage(String spuId) {
        System.out.println("生成静态页面"+spuId);
        itemPageService.staticPage(spuId);
    }
}
