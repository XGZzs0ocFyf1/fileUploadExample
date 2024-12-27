package a.v.g.wordApp.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Секретный ключ (автоматически генерируется безопасный симметричный ключ HMAC)
    private static final SecretKey SECRET_KEY = getSecretKey();

    // Время жизни токена (например, 1 час)
    private static final long EXPIRATION_TIME_MILLIS = 3600000;


    private static  SecretKey getSecretKey() {
        return Jwts.SIG.HS256.key().build();
    }

    /**
     * Генерация JWT токена.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
                .signWith(SECRET_KEY) // Подпись токена секретным ключом
                .compact();
    }

    /**
     * Проверка валидности токена.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(SECRET_KEY) // Установка ключа для валидации
                    .build()
                    .parseSignedClaims(token); // Парсинг токена
            return true;
        } catch (Exception e) {
            // В случае ошибок валидации токен недействителен
            return false;
        }
    }

    /**
     * Извлечение имени пользователя из токена.
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}