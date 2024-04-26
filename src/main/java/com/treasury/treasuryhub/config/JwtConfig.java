package com.treasury.treasuryhub.config;

import com.treasury.treasuryhub.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtConfig {

    public static final long Jwt_valid = 60 * 60 * 24 * 7 * 1000; // 7 days
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Jwt_valid))
                .signWith(key)
                .compact();
    }

    public <T> T extractClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims allClaims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claimsResolver.apply(allClaims);
    }

    public String extractEmailFromToken(String token) {
        return extractClaimFromToken(token, Claims::getSubject);
    }

    private Date extractExpirationDateFromToken(String token) {
        return extractClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        Date expirationDate = extractExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractEmailFromToken(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
