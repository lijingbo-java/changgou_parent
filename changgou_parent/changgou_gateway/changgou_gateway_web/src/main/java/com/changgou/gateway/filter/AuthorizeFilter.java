package com.changgou.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/9/3 22:35
 * @description ：全局过滤
 * @version: 1.0
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    public static final String UID = "uid";
    public static final String URL = "//login.changgou.com:9200/login?ReturnUrl=";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        HttpCookie cookie = request.getCookies().getFirst(UID);
        if (cookie != null) {
            String jti = cookie.getValue();
            if (jti != null) {
                //曾经登录过,如果已登录将令牌放入请求头中
                if (stringRedisTemplate.hasKey(jti)) {
                    //获取token令牌
                    String token = stringRedisTemplate.boundValueOps(jti).get();
                    //将令牌放入到请求头中
                    request.mutate().header("Authorization", "Bearer " + token);
                } else {
                    //过期了,重新登录
                    //响应,要求浏览器重新定向
                    response.setStatusCode(HttpStatus.SEE_OTHER);
                    //将跳转的路径加入到响应头里
                    try {//设置响应头  Location:http://login.changgou.com:9200/login?ReturnUrl=http://www.changgou.com/search?keywords=手机&brand=华为.....
                        String url = URL + URLEncoder.encode(request.getURI().getRawSchemeSpecificPart(), "utf-8");
                        response.getHeaders().set(HttpHeaders.LOCATION, url);
                        return response.setComplete();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            } else {//未登录
                //响应,要求浏览器重新定向
                response.setStatusCode(HttpStatus.SEE_OTHER);
                //将跳转的路径加入到响应头里
                try {//设置响应头  Location:http://login.changgou.com:9200/login?ReturnUrl=http://www.changgou.com/search?keywords=手机&brand=华为.....
                    String url = URL + URLEncoder.encode(request.getURI().getRawSchemeSpecificPart(), "utf-8");
                    response.getHeaders().set(HttpHeaders.LOCATION, url);
                    return response.setComplete();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } else {//未登录
            //响应,要求浏览器重新定向
            response.setStatusCode(HttpStatus.SEE_OTHER);
            //将跳转的路径加入到响应头里
            try {//设置响应头  Location:http://login.changgou.com:9200/login?ReturnUrl=http://www.changgou.com/search?keywords=手机&brand=华为.....
                String url = URL + URLEncoder.encode(request.getURI().getRawSchemeSpecificPart(), "utf-8");
                response.getHeaders().set(HttpHeaders.LOCATION, url);
                return response.setComplete();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        //放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
