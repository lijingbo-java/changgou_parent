package com.changgou.oauth2.service;

import com.changgou.oauth2.pojo.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/9/1 12:43
 * @description ：登录实现类
 * @version: 1.0
 */
@Service
public class LoginServiceImpl {
    @Autowired
    private LoadBalancerClient loadBalancerClient;//负载均衡,服务器集群,
    @Autowired//因为feign不能带请求头,所欲使用原始的RestTemplate调用,需要在启动类中实例化
    private RestTemplate restTemplate;
    @Value("${auth.clientId}")
    private String clientId;
    @Value("${auth.clientSecret}")
    private String clientSecret;

    public static final String USERAUTH = "user-auth";//服务名称常量

    //申请令牌
    public AuthToken apply(String username, String password) {
        //从集群中随机获取一台服务器(地址)
        ServiceInstance choose = loadBalancerClient.choose(USERAUTH);
        System.out.println(choose.getHost() + ">>>>>>>>>>>>>>>>>>>>>");
        String url = choose.getUri().toString() + "/oauth/token";

        //请求头
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", handlerHeader());

        //请求体
        LinkedMultiValueMap<String, String> boy = new LinkedMultiValueMap<>();
        boy.add("grant_type", "password");
        boy.add("username", username);
        boy.add("password", password);
        //请求实体
        HttpEntity<LinkedMultiValueMap<String, String>> requestEntity = new HttpEntity<>(boy, headers);

        ResponseEntity<Map> respones = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        //得到请求体
        Map<String, String> body = respones.getBody();

        return new AuthToken(body.get("jti"), body.get("refresh_token"), body.get("access_token"));
    }


    /**
     * 处理请求头
     *
     * @return
     */
    public String handlerHeader() {
        String s = clientId + ":" + clientSecret;
        byte[] encode = Base64Utils.encode(s.getBytes());
        return "Basic " + new String(encode);
    }
}
