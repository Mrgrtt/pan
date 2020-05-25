package com.haylen.pan.service.impl;

import com.haylen.pan.service.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author haylen
 * @date 2020-5-25
 */
@Service
@ConfigurationProperties("jwt")
@Setter
@Getter
public class TokenServiceImpl implements TokenService {
    private String secret;
    private long expiration;
    private String requestHeader;
    private String tokenHead;

    @Override
    public String builtToken(String subject) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder().setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .setSubject(subject)
                .signWith(key)
                .compact();
    }

    @Override
    public String getSubjectByToken(String token) throws TokenParserException {
        try {
            return Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            throw new TokenParserException(e.getMessage());
        }
    }
}
