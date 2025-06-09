package app.task_manager.auth.provider;

import app.task_manager.auth.exception.InvalidJwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.access-expiration-ms}")
    private long accessExpirationMs;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateAccessToken(Authentication authentication) {
        String username = authentication.getName();
        log.info("Generating ACCESS token for user='{}', expires in {} ms", username, accessExpirationMs);
        return buildToken(authentication.getName(), accessExpirationMs, "ACCESS");
    }

    public String generateRefreshToken(Authentication authentication) {
        String username = authentication.getName();
        log.info("Generating REFRESH token for user='{}', expires in {} ms", username, refreshExpirationMs);
        return buildToken(authentication.getName(), refreshExpirationMs, "REFRESH");
    }

    private String buildToken(String subject, long validity, String type) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validity);

        String token = Jwts.builder().setSubject(subject).setIssuedAt(now).setExpiration(expiry).claim("type", type).signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();

        log.debug("Built {} token for subject='{}', issuedAt={}, expiresAt={}", type, subject, now, expiry);
        return token;
    }

    public void validateAccessToken(String token) {
        validateToken(token, "ACCESS");
    }

    public void validateRefreshToken(String token) {
        validateToken(token, "REFRESH");
    }


    private void validateToken(String token, String expectedType) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();

            String type = claims.get("type", String.class);
            if (!expectedType.equals(type)) {
                log.warn("Token type mismatch: expected='{}', actual='{}'", expectedType, type);
                throw new InvalidJwtAuthenticationException("Invalid token type: " + type);
            }

            log.debug("{} token for subject='{}' is valid", expectedType, claims.getSubject());

        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("{} token validation failed: {}", expectedType, ex.getMessage());
            throw new InvalidJwtAuthenticationException("Invalid or expired JWT token");
        }
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();

        String username = claims.getSubject();
        log.debug("Extracted username='{}' from token", username);
        return username;
    }
}
