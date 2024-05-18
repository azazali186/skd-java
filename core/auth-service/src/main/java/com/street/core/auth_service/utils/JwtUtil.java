package com.street.core.auth_service.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "B84ED6CB1A10B57C0BF260811A2D1A903418EB9B09E9F03882B72562938D58F1";
    private static final long EXPIRATION_TIME = 1L * 24L * 60L * 60L * 1000L;
    // private static final long EXPIRATION_TIME = 5L * 1000L; // 5 seconds


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @SuppressWarnings("removal")
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return new Long((String) claims.get("id"));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) throws ExpiredJwtException, SignatureException {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | SignatureException e) {
            throw e;
        }
    }

    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException(null, null, "Token Expired");
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) throws ExpiredJwtException {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (ExpiredJwtException ex) {
            // Handle the expired token exception or rethrow it if necessary
            throw ex;
        }
    }

    public Boolean validateToken(String token, String userName) throws ExpiredJwtException {
        try {
            final String username = extractUsername(token);
            return (username.equals(userName) && !isTokenExpired(token));
        } catch (ExpiredJwtException ex) {
            // Handle the expired token exception or rethrow it if necessary
            throw ex;
        }
    }

    public Boolean validateToken(String token, Long userId) throws ExpiredJwtException {
        try {
            final Long user = extractUserId(token);
            return (Objects.equals(user, userId) && !isTokenExpired(token));
        } catch (ExpiredJwtException ex) {
            throw ex;
        }
    }

    public String generateToken(String userName, Long id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id.toString());
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static JwtUtil getInstance() {
        return new JwtUtil();
    }
}
