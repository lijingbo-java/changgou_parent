package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/28 9:44
 * @description ：启动引导引导类
 * @version: 1.0
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.changgou.feign")
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class,args);
    }
}
