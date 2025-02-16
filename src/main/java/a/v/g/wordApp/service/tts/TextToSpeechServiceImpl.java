package a.v.g.wordApp.service.tts;

import a.v.g.wordApp.service.s3.YCS3Api;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import yandex.cloud.api.ai.tts.v3.SynthesizerGrpc.SynthesizerStub;
import yandex.cloud.api.ai.tts.v3.Tts.AudioFormatOptions;
import yandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisRequest;
import yandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisResponse;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextToSpeechServiceImpl implements TextToSpeechService{

    private final SynthesizerStub synthesizerStub;
    private final YCS3Api s3service;

    //старый метод пишет в файл (для отладки оставил)
    public void synthesizeText(String text, String outputFilePath) {
        // Создаем запрос
        UtteranceSynthesisRequest request = UtteranceSynthesisRequest.newBuilder()
                .setText(text)
                .setOutputAudioSpec(AudioFormatOptions.newBuilder().build())
                .build();

        // Потоковый ответ
        synthesizerStub.utteranceSynthesis(request, new StreamObserver<UtteranceSynthesisResponse>() {
            FileOutputStream outputStream;

            {
                try {
                    outputStream = new FileOutputStream(outputFilePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNext(UtteranceSynthesisResponse response) {
                try {
                    outputStream.write(response.getAudioChunk().getData().toByteArray());
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    log.info("Chunk received");
                }
            }

            @Override
            public void onError(Throwable t) {
                log.error("Error: {}", t.getMessage());
            }

            @Override
            public void onCompleted() {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("Synthesis complete. Audio saved to " + outputFilePath);
            }
        });
    }


    //новый метод, пишет в
    @Async
    public CompletableFuture<String> asyncSynthesizeAndUploadText(String text) {
        // Создаём запрос
        UtteranceSynthesisRequest request = UtteranceSynthesisRequest.newBuilder()
                .setText(text)
                .setOutputAudioSpec(AudioFormatOptions.newBuilder().build())
                .build();

        // Подготовим будущий результат
        CompletableFuture<String> future = new CompletableFuture<>();

        // Будем временно записывать аудио-чанки в ByteArrayOutputStream
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        // Запускаем асинхронный gRPC вызов
        synthesizerStub.utteranceSynthesis(request, new StreamObserver<UtteranceSynthesisResponse>() {

            @Override
            public void onNext(UtteranceSynthesisResponse response) {
                try {
                    byteStream.write(response.getAudioChunk().getData().toByteArray());
                    log.info("Chunk received");
                } catch (Exception e) {
                    e.printStackTrace();
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onError(Throwable t) {
                log.error("Error: {}", t.getMessage());
                future.completeExceptionally(t);
            }

            @Override
            public void onCompleted() {
                try {
                    var folderName = "audio";
                    var fileType = "wav";
                    byte[] result = byteStream.toByteArray();
                    var uploadLink = s3service.uploadFile(folderName,  "audio-"+UUID.randomUUID()+".wav",result, fileType);
                    future.complete(uploadLink);
                    log.info("Synthesis complete. Bytes in future");
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }

}

