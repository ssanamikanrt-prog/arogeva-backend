package com.hospital.Arogeva.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Use a static 256-bit secret key so tokens survive server restarts!
    private final String jwtSecretString = "ArogevaSuperSecretKeyForJwtValidationThatIsAtLeast32Bytes!";
    private final Key jwtSecret = Keys.hmacShaKeyFor(jwtSecretString.getBytes(StandardCharsets.UTF_8));


    // 90 days for access token
    private final long jwtExpirationInMs = 1000L * 60 * 60 * 24 * 90;        //90days

    // 180 days for refresh token
    private final long refreshExpirationInMs = 1000L * 60 * 60 * 24 * 180;   //180 days


//    // 1 hour for access token
//    private final int jwtExpirationInMs = 3600000;
//
//    // 7 days for refresh token
//    private final int refreshExpirationInMs = 604800000;


//
//    private final long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 90; // 90 days
////	private final long EXPIRATION_TIME = 1000L * 60 * 1; // 1 minute
//
//    private final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 180; // 180 days



    public String generateAccessToken(String userIdStr) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userIdStr)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecret)
                .compact();
    }

    public String generateRefreshToken(String userIdStr) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpirationInMs);

        return Jwts.builder()
                .setSubject(userIdStr)
                .setIssuedAt(now)
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
