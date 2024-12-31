package a.v.g.wordApp.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private static SecretKey getSecretKey() {
        return Jwts.SIG.HS256.key().build();
    }

    private static final SecretKey SECRET_KEY = getSecretKey();






    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return parseClaims(token)
                .getPayload()
                .getSubject();
    }

    public Jws<Claims> parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY) // Установка ключа для валидации
                .build()
                .parseSignedClaims(token);
    }

    public String generateToken(String username, Date expirationDate) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token).getPayload();
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", new ArrayList<>());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }


    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



}
