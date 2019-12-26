package com.haylen.pan.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author haylen
 * @date 2019-12-26
 */
@Slf4j
final public class JwtUtil {
    @Value("${jwt.secret}")
    private static String secret;
    @Value("${jwt.expiration}")
    private static long expirationTime;
    @Value("${jwt.requestHeader}")
    private static String requestHeader;
    @Value("${jwt.tokenHead}")
    private static String tokenHead;

    public static String builtToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder().setIssuedAt(new Date())
                .setExpiration(getExpirationDate())
                .setSubject(username)
                .signWith(key)
                .compact();
    }

    public static String getUsernameByToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            log.info("jwt格式验证失败：{}", token);
            return null;
        }
    }

    private static Date getExpirationDate() {
        return new Date(System.currentTimeMillis() + expirationTime * 1000);
    }
}
