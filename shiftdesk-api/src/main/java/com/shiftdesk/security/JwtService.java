package com.shiftdesk.security;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {
  private final SecretKey key;
  public JwtService(@Value("${shiftdesk.jwt.secret}") String secret) { key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); }

  public String generate(Long employeeId) {
    long now = System.currentTimeMillis();
    return Jwts.builder().subject(String.valueOf(employeeId)).issuedAt(new Date(now))
        .expiration(new Date(now + 8 * 3600_000L)).signWith(key).compact();
  }
  public Long parse(String token) {
    return Long.valueOf(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject());
  }
}
