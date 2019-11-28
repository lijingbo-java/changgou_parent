package com.example.canatest;

import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCanalClient
@EnableFeignClients(basePackages = "com.changgou.business.feign")
public class CanaTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanaTestApplication.class, args);
	}
}
