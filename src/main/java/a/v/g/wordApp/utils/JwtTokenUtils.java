package a.v.g.wordApp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenUtils {

    private final SecretKey secret = getSecretKey();
    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    private SecretKey getSecretKey() {
        return Jwts.SIG.HS256.key().build();
    }


    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        List<String> roleList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roleList);
      //TODO  claims.put("email", userDetails.);
        Date issuedDate = new Date();
        var expiredDate = new Date(issuedDate.getTime()  + jwtLifetime.toMillis());

        return Jwts.builder()
                .claims()
                .add(claims)
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .and()
                .signWith(secret)
                .compact();

    }

    public String getUserName(String token){
        return getAllClaimsFromToken(token).getSubject();
    }

//    public List<String> getRoles(String token){
//        return getAllClaimsFromToken(token).get("roles", List.class);
//    }

    public List<String> getRoles(String token) {
        // Извлекаем список ролей как объект
        Object roles = getAllClaimsFromToken(token).get("roles");

        // Преобразуем в List<String>, если роли существуют
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .map(Object::toString) // Преобразуем каждый элемент в строку
                    .toList(); // Собираем обратно в список
        }

        // Если роли отсутствуют, возвращаем пустой список
        return List.of();
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .verifyWith(secret).build()
                .parseSignedClaims(token)
                .getPayload();
    }
}