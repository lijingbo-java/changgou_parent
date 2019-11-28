package com.changgou.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/14 14:43
 * @description ：文件处理启动类
 * @version: 1.0
 */
@SpringBootApplication
@EnableEurekaClient//服务发现到注册中心
public class FileApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class);
    }

}
