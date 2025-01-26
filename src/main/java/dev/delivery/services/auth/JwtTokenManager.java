package dev.delivery.services.auth;

import dev.delivery.dtos.auth.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenManager {
    public static final long TOKEN_VALIDITY = 10 * 60 * 60;
    @Value("${signing.key}")
    private String jwtSecret;

    public String generateJwtToken(AuthUser userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userid", userDetails.getId());
        claims.put("username", userDetails.getUsername());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roles);
        return Jwts
                .builder()
                .claims().empty().add(claims)
                .and()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(getKey())
                .compact();
    }

    public boolean validateJwtToken(String token, AuthUser userDetails) {
        final String username = getUsernameFromToken(token);
        final Claims claims = getClaims(token);
        Boolean isTokenExpired = claims.getExpiration().before(new Date());
        return (username.equals(userDetails.getUsername())) && !isTokenExpired;
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public Long getIdFromToken(String token) {
        return Long.valueOf(getClaims(token).get("userid").toString());
    }

    public List<String> getRolesFromToken(String token) {
        return List.of(getClaims(token).get("roles").toString());
    }

    public List<String> getRoles() {
        return getRolesFromToken(getJwtFromRequest());
    }

    public Long getId() {
        return getIdFromToken(getJwtFromRequest());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String getJwtFromRequest() {
        String authorizationHeader = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        return authorizationHeader.substring(7);
    }
}
