package a.v.g.wordApp.service.yandx;


import a.v.g.wordApp.model.yc.KeyInfo;
import a.v.g.wordApp.model.yc.TokenData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



@Service
public class PlanB {


    @Value("${cloud.yandex.translation.jsonFileName}")
    private String jsonFileName;
    @Value("${cloud.yandex.token-url}")
    private String tokenExchangeUrl;








    private  String getIamToken() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String encodedToken = getJwtToken();
        System.out.println(encodedToken);
        return exchangeJwtToIam(encodedToken);
    }

    private String exchangeJwtToIam(String encodedToken) throws JsonProcessingException {
        // Инициализируем RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Устанавливаем заголовки (Content-Type = application/json)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Тело запроса — JSON c полем jwt
        Map<String, String> body = new HashMap<>();
        body.put("jwt", encodedToken);

        // Формируем HttpEntity (содержимое + заголовки)
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // URL сервиса


        // Отправляем POST-запрос и получаем ответ
        ResponseEntity<String> response = restTemplate.exchange(
                tokenExchangeUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );


        // Выводим ответ (в данном случае строку)
        System.out.println("Response code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
        System.out.println("headers: " + response.getHeaders());

        var x = (new ObjectMapper()).readValue(response.getBody(), TokenData.class);
        System.out.println("Response body2: " + x);

        return "";
    }

    private  String getJwtToken() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String content = new String(Files.readAllBytes(Paths.get(jsonFileName)));
        KeyInfo keyInfo = (new ObjectMapper()).readValue(content, KeyInfo.class);

        System.out.println("генерируем токен для аккаунта: " + keyInfo.serviceAccountId());

        PemObject privateKeyPem;
        try (PemReader reader = new PemReader(new StringReader(keyInfo.privateKey()))) {
            privateKeyPem = reader.readPemObject();
        }

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyPem.getContent()));

        Instant now = Instant.now();

        // Формирование JWT.

        return Jwts.builder()
                .header().add("kid", keyInfo.id()).and()
                .issuer(keyInfo.serviceAccountId())
                .audience().add(tokenExchangeUrl)
                .and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(3600)))
                .signWith(privateKey, Jwts.SIG.PS256)
                .compact();
    }
}
