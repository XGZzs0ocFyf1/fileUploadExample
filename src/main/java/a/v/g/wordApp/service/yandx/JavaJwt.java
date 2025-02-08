package a.v.g.wordApp.service.yandx;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//yc iam key create --output D:\code\keys\translation-api-key --service-account-name translation-api


public class JavaJwt {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KeyInfo {
        public String id;
        public String service_account_id;
        public String private_key;
    }

    public static void main(String[] args) throws Exception {

        var jsonFileName = "D:\\code\\keys\\translation-api-key\\1.json";
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
        String encodedToken = Jwts.builder()
                .header().add("kid", keyId).and()

                .issuer(serviceAccountId)
                .audience().add("https://iam.api.cloud.yandex.net/iam/v1/tokens")
                .and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(3600)))
                .signWith(privateKey, Jwts.SIG.PS256)
                .compact();
        System.out.println(encodedToken);


//        curl \
//        --request POST \
//        --header 'Content-Type: application/json' \
//        --data '{"jwt": "<JWT-токен>"}' \
//        https://iam.api.cloud.yandex.net/iam/v1/tokens


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
        String url = "https://iam.api.cloud.yandex.net/iam/v1/tokens";

        // Отправляем POST-запрос и получаем ответ
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Выводим ответ (в данном случае строку)
        System.out.println("Response code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
        System.out.println("headers: " + response.getHeaders());
    }
}
