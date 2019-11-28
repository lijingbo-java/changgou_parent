package com.changgou.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/17 15:13
 * @description ：自定义全局过滤器
 * @version: 1.0
 */
@Component
public class IpFilter implements GlobalFilter,Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        System.out.println(request.getURI().toString());
        System.out.println("ip:"+request.getRemoteAddress().getHostName());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
