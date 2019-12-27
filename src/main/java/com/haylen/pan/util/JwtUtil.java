package com.haylen.pan.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author haylen
 * @date 2019-12-26
 */
@Slf4j
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expirationTime;
    @Value("${jwt.requestHeader}")
    private String requestHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public String builtToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder().setIssuedAt(new Date())
                .setExpiration(getExpirationDate())
                .setSubject(username)
                .signWith(key)
                .compact();
    }

    public String getUsernameByToken(String token) {
        try {
            return Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            log.info("jwt格式验证失败：{}", token);
            return null;
        }
    }

    private Date getExpirationDate() {
        return new Date(System.currentTimeMillis() + expirationTime * 1000);
    }
}
