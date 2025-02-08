package a.v.g.wordApp.service.s3;

import a.v.g.wordApp.exceptions.FileNotFoundInBucketException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListBucketAnalyticsConfigurationsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = "spring-boot-s3-exmaple";
    private final String PREFIX = "photos/";

    public String uploadFile( String filename, byte[] data, String contentType) {
        String key = PREFIX + filename;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(data));
        return key;
    }

    private ResponseInputStream<GetObjectResponse> downloadFile(String key) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        return s3Client.getObject(objectRequest);
    }

    public ResponseEntity<byte[]> downloadFileByName(String key) throws FileNotFoundInBucketException {
        try (var is = downloadFile(key)) {
            byte[] data = is.readAllBytes();
            var headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");
            headers.add(HttpHeaders.CONTENT_TYPE, is.response().contentType());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(data);
        } catch (Exception e) {
            throw new FileNotFoundInBucketException("");
        }
    }

}
