package com.example.lm.utils;

import io.jsonwebtoken.*;

import java.util.Date;

public class tokenUtil {
    private final static long expire = 7 * 24 * 3600; // 设置token过期时间为7天
    public final static String secret = "Thisisusedforxjtlusecurity"; //32位密钥

    public static String generateToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * expire);
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static Claims getClaimsByToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public static void main(String[] args) {
        String username = "exampleUser"; // 用于生成 JWT 的用户名
        String token = generateToken(username);
        System.out.println("Generated JWT token: " + token);
        System.out.println("解析：" + getClaimsByToken(token));

    }
}
