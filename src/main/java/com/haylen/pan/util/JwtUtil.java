package com.haylen.pan.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
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

    public static boolean validateToken(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) {
            return false;
        }
        return validateToken(token);
    }

    private static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.info("jwt格式验证失败：{}", token);
            return false;
        }
        return true;
    }

    private static String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(requestHeader);
        if (header == null) {
            return null;
        }
        if (!header.startsWith(tokenHead)) {
            return null;
        }
        /* 返回'Bearer '后面的部分 */
        return header.substring(tokenHead.length() + 1);
    }

    private static Date getExpirationDate() {
        return new Date(System.currentTimeMillis() + expirationTime * 1000);
    }
}
