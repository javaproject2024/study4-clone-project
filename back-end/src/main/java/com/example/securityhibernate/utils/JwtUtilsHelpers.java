package com.example.securityhibernate.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtilsHelpers {

    @Value("${jwt.privateKey}")
    private String privateKey;

    // Set time jwt 1h
    private long expiredTime = 60 * 60 * 1000;

    public String generateToken(String data, String username) {

        SecretKey key = Keys.hmacShaKeyFor(privateKey.getBytes());

        Date date = new Date();
        long currentDateMilis = date.getTime() + expiredTime;
        Date expiredDate = new Date(currentDateMilis);

        String jwt = Jwts.builder()
                .setSubject(data)
                .claim("username", username)
                .signWith(key) //Key mã hóa
                .setExpiration(expiredDate) //Set time
                .compact();

        System.out.println("Token: "+jwt);
        return jwt;
    }

    // Xác nhận token thông qua private key
    public boolean verifyToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(privateKey.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public String getDataFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(privateKey.getBytes());
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        }catch (Exception e) {
            return "";
        }
    }

    // Lấy thông tin username thông qua token
    public String getUsernameByToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(privateKey.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("username", String.class);
    }

}