package a.v.g.wordApp.service;

import a.v.g.wordApp.service.s3.YCS3Api;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ImageGenerationService {

    private final YCS3Api s3Service;

    @Async
    public CompletableFuture<String> generateAndUploadImage(String word) {
        String folderName = "images";
        byte[] imageBytes = generateImageBytes(word);
        return CompletableFuture.supplyAsync( () ->
            s3Service.uploadFile(folderName, word + "-image.png", imageBytes, "image/png")
        );
    }

    private byte[] generateImageBytes(String word) {
        return new byte[0]; // Пример: вернуть байты изображения
    }
}
