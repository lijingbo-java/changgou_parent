package com.changgou;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/18 10:42
 * @description ：
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
public class JwtTest {
    @Test
    public void jwtTest() {
        long now = System.currentTimeMillis();
        long exp = now + 1000 * 30;//30秒过期
        JwtBuilder builder = Jwts.builder()
                .setId("888")   //设置唯一编号
                .setSubject("小白")//设置主题  可以是JSON数据
                .setIssuedAt(new Date())//设置签发日期
                //.setExpiration(new Date(exp))
                .claim( "roles","admin" )
                .signWith(SignatureAlgorithm.HS256, "itacst");//设置签名 使用HS256算法，并设置SecretKey(字符串)
        //构建 并返回一个字符串
        System.out.println(builder.compact());
    }

    @Test
    public void jxJwt() {
        String compactJwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE1NjYwOTgwODEsInJvbGVzIjoiYWRtaW4ifQ.prXbZ-PiyIGQ8iRcn6tDpa80j7Bsoffj7nJlz-iPno0";
        Claims itacst = Jwts.parser().setSigningKey("itacst").parseClaimsJws(compactJwt).getBody();
        System.out.println(itacst);
        System.out.println(itacst.get("roles"));


    }
}