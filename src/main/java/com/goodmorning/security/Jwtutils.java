// package com.goodmorning.security;

// import io.jsonwebtoken.*;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;

// import javax.crypto.SecretKey;
// import java.util.Date;

// @Component
// public class JwtUtils {

//     @Value("${app.jwt.secret}")
//     private String jwtSecret;

//     @Value("${app.jwt.expiration-ms}")
//     private long jwtExpirationMs;

//     private SecretKey getSigningKey() {
//         // If secret is hex-encoded use Decoders, otherwise use plain bytes
//         try {
//             byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
//             return Keys.hmacShaKeyFor(keyBytes);
//         } catch (Exception e) {
//             return Keys.hmacShaKeyFor(jwtSecret.getBytes());
//         }
//     }

//     public String generateToken(String email) {
//         return Jwts.builder()
//                 .subject(email)
//                 .issuedAt(new Date())
//                 .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
//                 .signWith(getSigningKey())
//                 .compact();
//     }

//     public String getEmailFromToken(String token) {
//         return Jwts.parser()
//                 .verifyWith(getSigningKey())
//                 .build()
//                 .parseSignedClaims(token)
//                 .getPayload()
//                 .getSubject();
//     }

//     public boolean validateToken(String token) {
//         try {
//             Jwts.parser()
//                 .verifyWith(getSigningKey())
//                 .build()
//                 .parseSignedClaims(token);
//             return true;
//         } catch (JwtException | IllegalArgumentException e) {
//             return false;
//         }
//     }
// }
