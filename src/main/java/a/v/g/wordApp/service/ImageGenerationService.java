package a.v.g.wordApp.service;

import a.v.g.wordApp.service.s3.S3Service;
import org.springframework.stereotype.Service;

@Service
public class ImageGenerationService {

    private final S3Service s3Service;

    public ImageGenerationService(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public String generateAndUploadImage(String word) {
        // Логика для генерации изображения
        byte[] imageBytes = generateImageBytes(word); // Метод для создания изображения (например, через API)

        // Загружаем файл в S3
        String imageUrl = s3Service.uploadFile(word + "-image.png", imageBytes, "image/png");
        return imageUrl;  // Ссылку на загруженное изображение
    }

    private byte[] generateImageBytes(String word) {
        // Логика генерации изображения и преобразования его в байты
        return new byte[0]; // Пример: вернуть байты изображения
    }
}
