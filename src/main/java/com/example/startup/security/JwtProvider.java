package com.example.startup.security;

import com.example.startup.exception.JwtException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String key;
    @Value("${jwt.token.ttl}")
    private Long ttl;

    @Value("${refresh.expiration.millis}")
    private Long refreshMillis;


    public String generateToken(String userKey) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(userKey)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ttl))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String generateRefreshToken(String userKey){
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(userKey)
                .setIssuedAt(new Date(now))
                .claim("type", "refresh")
                .setExpiration(new Date(now + refreshMillis))
                .signWith(SignatureAlgorithm.HS256,key)
                .compact();
    }

    public String getKeyFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token has expired: " + e.getMessage());
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            throw new JwtException("Invalid JWT token: " + e.getMessage());
        }
    }


    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token has expired: " + e.getMessage());
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            throw new JwtException("Invalid JWT token: " + e.getMessage());
        }
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isRefreshToken(String token) {
        Claims claims = validateToken(token);
        return "refresh".equals(claims.get("type"));
    }
}