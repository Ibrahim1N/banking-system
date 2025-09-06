package banking_system.bankingSystem.dto;

import banking_system.bankingSystem.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "my_super_secret_jwt_key_for_banking_system_123456";

    // Token validity (e.g., 24 hours)
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // in milliseconds

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // ---------------- GENERATE TOKEN ----------------
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // store email in token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ---------------- EXTRACT USERNAME (EMAIL) ----------------
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // ---------------- VALIDATE TOKEN ----------------
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ---------------- GET CLAIMS ----------------
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
