package a.v.g.wordApp.service.yandx;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.ToString;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.http.*;
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

//yc iam key create --output D:\code\keys\translation-api-key --service-account-name translation-api


public class PlanB {


    private static String jsonFileName = "D:\\code\\keys\\translation-api-key\\1.json";
    private static final String tokenExchangeUrl = "https://iam.api.cloud.yandex.net/iam/v1/tokens";

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KeyInfo {
        public String id;
        public String service_account_id;
        public String private_key;
    }

   @ToString
    public static class TokenData {
        public String iamToken;
        public String expiresAt;
    }

    record TokenData2(
            String iamToken,
            String expiresAt
    ){}

    public static void main(String[] args) throws Exception {
        getIamToken();
    }


    private static String getIamToken() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String encodedToken = getJwtToken();
        System.out.println(encodedToken);
        return exchangeJwtToIam(encodedToken);
    }

    private static String exchangeJwtToIam(String encodedToken) throws JsonProcessingException {
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

        var x = (new ObjectMapper()).readValue(response.getBody(), TokenData2.class);
        System.out.println("Response body2: " + x);

        return "";
    }

    private static String getJwtToken() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String content = new String(Files.readAllBytes(Paths.get(jsonFileName)));
        KeyInfo keyInfo = (new ObjectMapper()).readValue(content, KeyInfo.class);

        String privateKeyString = keyInfo.private_key;
        String serviceAccountId = keyInfo.service_account_id;
        String keyId = keyInfo.id;

        PemObject privateKeyPem;
        try (PemReader reader = new PemReader(new StringReader(privateKeyString))) {
            privateKeyPem = reader.readPemObject();
        }

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyPem.getContent()));

        Instant now = Instant.now();

        // Формирование JWT.

        return Jwts.builder()
                .header().add("kid", keyId).and()
                .issuer(serviceAccountId)
                .audience().add(tokenExchangeUrl)
                .and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(3600)))
                .signWith(privateKey, Jwts.SIG.PS256)
                .compact();
    }
}
