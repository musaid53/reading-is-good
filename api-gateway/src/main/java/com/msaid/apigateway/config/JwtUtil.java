package com.msaid.apigateway.config;
import com.msaid.apigateway.dto.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.*;
import java.util.function.Function;


@Component
@Log4j2
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.validSec}")
    private Long validSec;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    public boolean isInvalid(String token) {
        try {
            return isTokenExpired(token);
        }catch (Exception e){
            log.error("token parse error: {}",e.getLocalizedMessage());
            return true;
        }
    }

    public String generate(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUsername());
        claims.put("role", user.getRoles());
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + (validSec * 1000));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }


    public Boolean validateToken(String token, User userDetails) {
        String userName = getUserNameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    public boolean hasAdminRole(String token) {
        List<String> roles = (List<String>) getAllClaimsFromToken(token).get("role");
        return roles.contains("admin");
    }
}
