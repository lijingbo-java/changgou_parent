package com.changgou.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/****
 * @Author:lx
 * @Description:
 * @Date 2019/7/26 18:15
 *****/
@SpringBootApplication
@EnableEurekaClient
public class WebGateWayApplication {
    

    public static void main(String[] args) {
        SpringApplication.run(WebGateWayApplication.class, args);
    }

}