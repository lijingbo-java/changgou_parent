package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/16 21:37
 * @description ：
 * @version: 1.0
 */
@SpringBootApplication//1.自动配置上下文,2.扫包,3.配置文件类
@EnableEurekaClient//
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class);
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                return Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
            }
        };
    }
}


