package com.example.shop.security;

import com.example.shop.model.UserAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final Key key;
    private final long accessTokenValidityMs;
    private final long refreshTokenValidityMs;

    // ВАЖНО: один явный конструктор, Spring будет использовать его
    public JwtTokenProvider(
            UserDetailsService userDetailsService,
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-token-expiration-ms:900000}") long accessTokenValidityMs,
            @Value("${app.jwt.refresh-token-expiration-ms:604800000}") long refreshTokenValidityMs
    ) {
        this.userDetailsService = userDetailsService;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityMs = accessTokenValidityMs;
        this.refreshTokenValidityMs = refreshTokenValidityMs;
    }

    public String generateAccessToken(UserAccount user, Long sessionId) {
        return buildToken(user, sessionId, accessTokenValidityMs, "ACCESS");
    }

    public String generateRefreshToken(UserAccount user, Long sessionId) {
        return buildToken(user, sessionId, refreshTokenValidityMs, "REFRESH");
    }

    private String buildToken(UserAccount user, Long sessionId, long validityMs, String type) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiry = Date.from(now.plusMillis(validityMs));

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", type);
        claims.put("sid", sessionId);
        claims.put("role", user.getRole()); // если у тебя поле иначе называется — поправь тут

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiry)
                .addClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean validateToken(String token, String expectedType) {
        try {
            Claims claims = parseClaims(token);
            String type = claims.get("type", String.class);
            if (!expectedType.equals(type)) {
                return false;
            }
            Date expiration = claims.getExpiration();
            return expiration != null && expiration.after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, "ACCESS");
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, "REFRESH");
    }

    public Long getSessionId(String token) {
        Claims claims = parseClaims(token);
        Object sid = claims.get("sid");
        if (sid == null) {
            return null;
        }
        if (sid instanceof Integer i) {
            return i.longValue();
        }
        if (sid instanceof Long l) {
            return l;
        }
        if (sid instanceof String s) {
            return Long.parseLong(s);
        }
        return null;
    }

    public Date getExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }
}
