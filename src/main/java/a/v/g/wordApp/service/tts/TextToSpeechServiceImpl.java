package a.v.g.wordApp.service.tts;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yandex.cloud.api.ai.tts.v3.SynthesizerGrpc.SynthesizerStub;
import yandex.cloud.api.ai.tts.v3.Tts.AudioFormatOptions;
import yandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisRequest;
import yandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisResponse;

import java.io.FileOutputStream;

@Service
@RequiredArgsConstructor
public class TextToSpeechServiceImpl implements TextToSpeechService{

    private final SynthesizerStub synthesizerStub;

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
                    System.out.println("Chunk received");
                }
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Synthesis complete. Audio saved to " + outputFilePath);
            }
        });
    }
}

