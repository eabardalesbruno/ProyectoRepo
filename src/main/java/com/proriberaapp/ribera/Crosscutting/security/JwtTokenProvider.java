package com.proriberaapp.ribera.Crosscutting.security;

import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.enums.Permission;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.admin}")
    private long jwtExpirationMs;

    public String generateTokenAdmin(UserAdminEntity userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .claim("permissions", userDetails.getPermission())
                .claim("id", userDetails.getUserAdminId())
                .claim("state", userDetails.getStatus())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getKey(jwtSecret))
                .compact();
    }


    public String generateToken(UserClientEntity subject) {
        return Jwts.builder()
                .setSubject(subject.getUsername())
                .claim("roles", subject.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(getKey(jwtSecret))
                .compact();
    }

    public Integer getIdFromToken(String token) {
        return getClaimsFromToken(token.substring(7)).get("id", Integer.class);
    }

    public List<Permission> getPermissionsFromToken(String token) {
        return getClaimsFromToken(token.substring(7)).get("permissions", List.class);
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey(jwtSecret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey(jwtSecret)).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private Key getKey(String secret) {
        byte[] keyBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
