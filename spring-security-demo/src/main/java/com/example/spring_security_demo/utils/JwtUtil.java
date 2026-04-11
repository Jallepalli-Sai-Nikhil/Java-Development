package com.example.spring_security_demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "mysecretkey";

    private final long ACCESS_EXP = 1000 * 60 * 15;
    private final long REFRESH_EXP = 1000 * 60 * 60 * 24;

    public String generateAccessToken(String username) {
        return createToken(username, ACCESS_EXP);
    }

    public String generateRefreshToken(String username) {
        return createToken(username, REFRESH_EXP);
    }

    private String createToken(String username, long exp) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + exp))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isValid(String token) {
        return !getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
