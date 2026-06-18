package com.hospital.Arogeva.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Use a static 256-bit secret key so tokens survive server restarts!
    private final String jwtSecretString = "ArogevaSuperSecretKeyForJwtValidationThatIsAtLeast32Bytes!";
    private final Key jwtSecret = Keys.hmacShaKeyFor(jwtSecretString.getBytes(java.nio.charset.StandardCharsets.UTF_8));

    // 24 hours
    private final int jwtExpirationInMs = 86400000;

    public String generateToken(Authentication authentication) {
        String userIdStr = authentication.getName();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userIdStr)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(jwtSecret)
                .compact();
    }

    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            System.err.println("Exception in JwtTokenProvider.validateToken:");
            ex.printStackTrace();
        }
        return false;
    }


}
