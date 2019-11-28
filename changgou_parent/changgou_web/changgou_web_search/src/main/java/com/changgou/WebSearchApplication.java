package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/22 20:01
 * @description ：
 * @version: 1.0
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.changgou.search.feign")
public class WebSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSearchApplication.class);
    }
}
