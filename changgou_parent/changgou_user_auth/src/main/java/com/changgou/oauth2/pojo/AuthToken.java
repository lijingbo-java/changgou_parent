package com.changgou.oauth2.pojo;

import java.io.Serializable;

public class AuthToken implements Serializable {

    //访问token就是短令牌，用户身份令牌(jti)
    String jti;
    //刷新token(refresh_token)
    String refreshToken;
    //jwt令牌(access_token)
    String accessToken;

    public AuthToken(String jti, String refreshToken, String accessToken) {
        this.jti = jti;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}