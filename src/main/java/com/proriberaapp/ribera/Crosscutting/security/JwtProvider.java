package com.proriberaapp.ribera.Crosscutting.security;

import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.entities.UserPromoterEntity;
import com.proriberaapp.ribera.Domain.enums.Permission;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.admin}")
    private long jwtExpirationMs;

    public String generateTokenPromoter(UserPromoterEntity userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .claim("id", userDetails.getUserPromoterId())

                .setSubject(userDetails.getUsername())

                .claim("document", userDetails.getDocumentNumber())
                .claim("roles", userDetails.getAuthorities())
                .claim("permissions", userDetails.getPermission())
                .claim("state", userDetails.getStatus())

                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)

                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateTokenAdmin(UserAdminEntity userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .claim("id", userDetails.getUserAdminId())

                .setSubject(userDetails.getUsername())

                .claim("document", userDetails.getDocumentNumber())
                .claim("roles", userDetails.getAuthorities())
                .claim("permissions", userDetails.getPermission())
                .claim("state", userDetails.getStatus())

                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)

                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserClientEntity subject) {
        return Jwts.builder()
                .setSubject(subject.getUsername())
                .claim("roles", subject.getAuthorities())
                .claim("id", subject.getUserClientId())
                .claim("state", subject.getStatus())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(getKey())
                .compact();
    }

    public Integer getIdFromToken(String token) {
        return getClaimsFromToken(token.substring(7)).get("id", Integer.class);
    }

    public String getDocumentFromToken(String token) {
        return getClaimsFromToken(token.substring(7)).get("document", String.class);
    }

    public List<Permission> getPermissionsFromToken(String token) {
        return getClaimsFromToken(token.substring(7)).get("permissions", List.class);
    }

    public String getAuthorityFromToken(String token) {
        Claims claims = getClaimsFromToken(token.substring(7));
        List<Map<String, String>> roles = claims.get("roles", List.class);
        if (roles != null && !roles.isEmpty()) {
            return roles.get(0).get("authority");
        }
        return null;
    }


    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
