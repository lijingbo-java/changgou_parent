package com.changgou.filter;

import com.changgou.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/18 20:49
 * @description ：登录全局过滤器
 * @version: 1.0
 */
@Component
public class LoginFilter implements GlobalFilter, Ordered {

    public static final String TOKEN = "token";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();//获取路径
        if (!path.contains("/system/admin/login")) {//如果路径包含则表示不是来登录的
            String token = request.getHeaders().getFirst(TOKEN);
            //不是来登录的先判断有没有登录,如果有token则表示已经登录,如果没有令牌则表示没有登录
            if (token != null) {
                try {
                    JwtUtil.parseJWT(token);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
            } else {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();

            }

        }
        //如果是来登录,直接放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
