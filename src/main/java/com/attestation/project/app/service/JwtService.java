package com.attestation.project.app.service;

import com.attestation.project.app.model.db.entity.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    // метод извлечения имени пользователя по токену
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // генерация токена
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof Customer customCustomerDetails) {
            claims.put("id", customCustomerDetails.getId());
            claims.put("username", customCustomerDetails.getUsername());
            claims.put("firstname", customCustomerDetails.getFirstname());
            claims.put("email", customCustomerDetails.getEmail());
            claims.put("status", customCustomerDetails.getStatus());
            claims.put("role", customCustomerDetails.getRole());
        }
        return generateToken(claims, userDetails);
    }

    // проверка подлинности токена
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // извлечения данных из токена
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    // генерация токена
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    // актуальность токена
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // дата окончания действия токена
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // извлечение всех данных из токена
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    // получения ключа для подписи токена
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
