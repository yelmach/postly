package postly.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import postly.entity.UserEntity;

@Component
public class JwtProvider {

    @Value("${app.jwt.secretKey}")
    private String secretKey;

    @Value("${app.jwt.expirationTime}")
    private long expirationTime;

    public String generateToken(UserEntity user) {
        try {
            Date now = new Date();
            Date expirationDate = new Date(now.getTime() + expirationTime);

            return Jwts.builder()
                    .setSubject(user.getUsername())
                    .setIssuedAt(now)
                    .setExpiration(expirationDate)
                    .signWith(getSigningKey())
                    .compact();
        } catch (Exception e) {
            throw new JwtException("Failed to generate JWT token", e);
        }
    }

    private Key getSigningKey() {
        try {
            byte[] keyBytes = secretKey.getBytes();
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            throw new JwtException("Failed to create JWT signing key", e);
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token is expired", e);
        } catch (SignatureException e) {
            throw new JwtException("JWT token signature validation failed", e);
        } catch (Exception e) {
            throw new JwtException("invalid JWT token", e);
        }
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token is expired", e);
        } catch (SignatureException e) {
            throw new JwtException("JWT token signature validation failed", e);
        } catch (Exception e) {
            throw new JwtException("invalid JWT token", e);
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }
}
