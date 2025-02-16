package a.v.g.wordApp.service.yandx.yart;

import a.v.g.wordApp.model.yc.yart.rq.AspectRatio;
import a.v.g.wordApp.model.yc.yart.rq.GenerationOptions;
import a.v.g.wordApp.model.yc.yart.rq.YMessage;
import a.v.g.wordApp.model.yc.yart.rq.YModel;
import a.v.g.wordApp.model.yc.yart.rs.GenerationRs1;
import a.v.g.wordApp.model.yc.yart.rs.ImageGenerationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class YArtImgService implements ImageService{


    @Value("${cloud.yandex.art.generation.url}")
    private String imgGenUrl;
    @Value("${cloud.yandex.art.polling.url}")
    private String imgPollUrl;
    String yFolder;
    @Value("${YANDEX_TTS_TOKEN}")
    String ycToken;

    @Override
    public CompletableFuture<String> generateImgAndSaveIt(String text) {
       return  generateImageWithPolling(imgGenUrl, imgPollUrl, text, ycToken);
    }


    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))  // Таймаут для подключения
            .build();



    public CompletableFuture<String> generateImageWithPolling(String imgGenUrl, String imgPollUrl, String text,
                                                              String ycToken)  {
        String prompt = generatePrompt(text);

        System.out.println(prompt);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(prompt))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ycToken)
                .uri(URI.create(imgGenUrl))
                .timeout(Duration.ofSeconds(30))  // Таймаут запроса на генерацию
                .build();

        // 2. Отправляем первый запрос на генерацию
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(response -> {
                    // Если код ответа не 200, завершаем с ошибкой
                    if (response.statusCode() != 200) {
                        System.out.println("Failed to start image generation");
                        System.out.println(response.statusCode());
                        System.out.println(response.body());
                        return CompletableFuture.failedFuture(new RuntimeException("Failed to start image generation"));
                    }

                    // Извлекаем ID из ответа для дальнейших запросов
                    String id = null;
                    try {
                        id = extractGenerationId(response.body());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    // 3. После успешного создания генерируем запросы на проверку статуса
                    return pollForImage(ycToken, imgPollUrl, id, 0);
                });
    }

    private String generatePrompt(String word){

        // Создаем объект Java
        GenerationOptions generationOptions = GenerationOptions.builder()
                .seed("1863")
                .aspectRatio(new AspectRatio("2", "1"))
                .build();

        var prompt = "Пришествие на Терру, туземцы приветствуют богов, 10000BC, Африка, hd full wallpaper, четкий фокус, множество сложных деталей, глубина кадра, вид сверху";
        YMessage message = new YMessage("1", prompt);


        var model = YModel.builder()
                .modelUri("art://" + yFolder + "/yandex-art/latest")
                .generationOptions(generationOptions)
                .messages(List.of(message).toArray(YMessage[]::new))
                .build();

        // Сериализуем объект Java в JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private String extractGenerationId(String responseBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        var rs = objectMapper.readValue(responseBody, GenerationRs1.class);
        return rs.id();
    }

    private CompletableFuture<String> pollForImage(String ycToken, String imgPollUrl, String id, int attempt) {
        // 4. Запускаем рекурсию для выполнения проверок статуса генерации
        int maxAttempts = 3;  // Максимальное количество попыток

        // Создаём запрос на получение статуса
        HttpRequest statusRequest = HttpRequest.newBuilder()
                .uri(URI.create(imgPollUrl + "/" + id))
                .timeout(Duration.ofSeconds(30))  // Таймаут для запроса статуса
                .header("Authorization", "Bearer " + ycToken)
                .build();

        // Выполняем асинхронный запрос статуса
        return client.sendAsync(statusRequest, HttpResponse.BodyHandlers.ofString())
                .thenCompose(response -> {
                    if (response.statusCode() != 200) {
                        return CompletableFuture.failedFuture(new RuntimeException("Failed to get image status"));
                    }

                    // Парсим ответ
                    ImageGenerationResponse imageResponse = null;
                    try {
                        imageResponse = parseImageResponse(response.body());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    // Проверяем, готова ли картинка
                    if (imageResponse.done()) {
                        // Если картинка готова, возвращаем её данные
                        return CompletableFuture.completedFuture(parseImage(imageResponse));  // Возвращаем картинку
                    }

                    // Если картинка не готова, ждём и пробуем снова (с увеличением количества попыток)
                    if (attempt < maxAttempts) {
                        // Задержка перед следующим запросом
                        try {
                            long millis = 5000L * (attempt + 1);
                            System.out.println("Waiting for " + millis + " millis");
                            Thread.sleep(millis);  // Задержка перед повторной попыткой
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return CompletableFuture.failedFuture(e);
                        }
                        return pollForImage(ycToken, imgPollUrl, id, attempt + 1);  // Рекурсия с увеличением попыток
                    }

                    // Если не удалось получить картинку после максимального числа попыток
                    return CompletableFuture.failedFuture(new RuntimeException("Image generation timed out after " + maxAttempts + " attempts"));
                });
    }


    private ImageGenerationResponse parseImageResponse(String responseBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(responseBody, ImageGenerationResponse.class);
    }

    private String parseImage(ImageGenerationResponse response) {
        return response.response().image();
    }
}
