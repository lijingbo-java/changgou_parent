package com.changgou.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/21 23:36
 * @description ：
 * @version: 1.0
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.changgou.feign")
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class);
    }
}
