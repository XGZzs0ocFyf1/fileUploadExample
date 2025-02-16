package a.v.g.wordApp.config;


import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

import static software.amazon.awssdk.http.apache.ApacheHttpClient.create;

@Configuration
@Getter
public class S3Config {

    @Value("${cloud.aws.s3-access-key}")
    private String accessKey;
    @Value("${cloud.aws.s3-secret-key}")
    private String secretKey;
    @Value("${cloud.aws.s3-endpoint}")
    private String endpoint;
    @Value("${cloud.aws.region}")
    private String region;

    private static String ACCESS_KEY;  // <идентификатор_статического_ключа>
    private static String SECRET_KEY;
    private static String ENDPOINT;
    private static String REGION;
    @Getter
    private final String BUCKET_NAME = "spring-boot-s3-exmaple";

    @PostConstruct
    public void init() {
        ACCESS_KEY = this.accessKey;
        SECRET_KEY = this.secretKey;
        ENDPOINT = this.endpoint;
        REGION = this.region;
    }


    @Bean
    public S3Client amazonS3() {

        AwsCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);

        return S3Client.builder()
                .httpClient(create())
                .region(Region.of(REGION))
                .endpointOverride(URI.create(ENDPOINT))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

    }
}
