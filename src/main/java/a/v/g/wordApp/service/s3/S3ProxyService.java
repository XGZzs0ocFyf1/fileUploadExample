//package a.v.g.wordApp.service.s3;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//
//import java.io.InputStream;
//import java.net.URL;
//import java.util.Date;
//
//@RequiredArgsConstructor
//@Service
//public class S3ProxyService {
//
//    private final AmazonS3 s3Client;
//
//    private static final String BUCKET_NAME = "test.bucket.v1"; // Название вашего бакета
//
//    public URL generatePresignedUrl(String objectKey) {
//        // Устанавливаем срок действия ссылки (например, 1 час)
//        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
//
//        // Генерируем временную ссылку
//        GeneratePresignedUrlRequest generatePresignedUrlRequest =
//                new GeneratePresignedUrlRequest(BUCKET_NAME, objectKey);
//                       // .withExpiration(expiration);
//
//        return s3Client.generatePresignedUrl(generatePresignedUrlRequest);
//    }
//
//    public S3Object getObject(String objectKey) {
//        return s3Client.getObject(BUCKET_NAME, objectKey);
//    }
//
//    public InputStream getFileAsStream(String fileName) {
//        try {
//            return s3Client.getObject(BUCKET_NAME, fileName).getObjectContent();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Ошибка при получении файла из хранилища", e);
//        }
//    }
//
//}
