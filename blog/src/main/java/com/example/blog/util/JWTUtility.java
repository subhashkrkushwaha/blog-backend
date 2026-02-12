package com.example.blog.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtility {
    private final String secret = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";
    private final  Key signingKey;
    public JWTUtility() {
        this.signingKey   = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails userDetails) {
      return  Jwts.builder()
              .setSubject(userDetails.getUsername())
              .claim("roles",
                      userDetails.getAuthorities()
                              .stream()
                              .map(GrantedAuthority::getAuthority)
                              .toList()
              )
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
              .signWith(signingKey ,SignatureAlgorithm.HS256)
              .compact();
    }
    public String extractUserEmail(String token) {
        return  Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean validateToken(String token) {
       try {
           Jwts.parserBuilder()
                   .setSigningKey(signingKey)
                   .build()
                   .parseClaimsJws(token);
           return  true;
       }catch (Exception e){
           return false;
       }
    }
}
